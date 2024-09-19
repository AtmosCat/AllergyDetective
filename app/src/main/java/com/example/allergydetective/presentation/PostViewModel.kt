package com.example.allergydetective.presentation

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.allergydetective.data.model.user.Comments
import com.example.allergydetective.data.model.user.Post
import com.example.allergydetective.data.model.user.Reply
import com.example.allergydetective.data.model.user.Report
import com.example.allergydetective.data.model.user.User
import com.example.allergydetective.presentation.community.postdetail.CommentsAdapter
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.HttpException
import java.io.IOException
import java.util.UUID

class PostViewModel (application: Application) : AndroidViewModel(application) {

    private val db = FirebaseFirestore.getInstance()

    private val _allPosts = MutableLiveData<List<Post>>()
    val allPosts : LiveData<List<Post>> get() = _allPosts

    private val _filteredPosts = MutableLiveData<List<Post>>()
    val filteredPosts : LiveData<List<Post>> get() = _filteredPosts

    private val _currentUser = MutableLiveData<User?>()
    val currentUser : LiveData<User?> get() = _currentUser

    private val _searchKeyword = MutableLiveData<String>()
    val searchKeyword : LiveData<String> get() = _searchKeyword

    private val _selectedCategories = MutableLiveData<MutableList<String>>()
    val selectedCategories : LiveData<MutableList<String>> get() = _selectedCategories

      private val _selectedSearchOption = MutableLiveData<String>()
    val selectedSearchOption : LiveData<String> get() = _selectedSearchOption

    private val _temporaryImageUrls = MutableLiveData<List<String>>()
    val temporaryImageUrls : LiveData<List<String>> get() = _temporaryImageUrls


    fun getAllPosts() {
        db.collection("post")
            // 가장 최근에 추가된 데이터부터 새로 정렬
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e(TAG, "getAllPosts() failed! : ${exception.message}")
                    handleException(exception)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val posts = mutableListOf<Post>()
                    for (document in snapshot.documents) {
                        val post = document.toObject(Post::class.java)?.copy(id = document.id)
                        post?.let { posts.add(it) }
                    }
                    _allPosts.value = posts
//                    _filteredPosts.value = posts
                } else {
                    // 예외처리: 스냅샷이 null일 때 처리
                }
            }
    }

    fun setSearchKeyword(keyword: String) {
        viewModelScope.launch {
            runCatching {
                _searchKeyword.value = keyword
            }.onFailure {
                Log.e(TAG, "setSearchKeyword() failed! : ${it.message}")
                handleException(it)
            }
        }
    }

    fun setCategories(categories: MutableList<String>) {
        viewModelScope.launch {
            runCatching {
                _selectedCategories.value = categories
            }.onFailure {
                Log.e(TAG, "setCategories() failed! : ${it.message}")
                handleException(it)
            }
        }
    }

    fun setSearchOption(option: String) {
        viewModelScope.launch {
            runCatching {
                _selectedSearchOption.value = option
            }.onFailure {
                Log.e(TAG, "setSearchOption() failed! : ${it.message}")
                handleException(it)
            }
        }
    }

    fun getFilteredPosts() {
        viewModelScope.launch {
            runCatching {
                val allData = allPosts.value ?: emptyList()
                val categories = selectedCategories.value ?: emptyList()
                val searchOption = selectedSearchOption.value
                val searchKeyword = searchKeyword.value ?: ""

                val filteredPosts = mutableListOf<Post>()

                if (categories.isNotEmpty() && searchKeyword.isNotBlank()) {
                    for (category in categories) {
                        when (searchOption) {
                            "제목 검색" -> {
                                filteredPosts += allData.filter { post ->
                                    post.category == category
                                            && post.title.contains(searchKeyword)
                                }
                            }
                            "제목+내용 검색" -> {
                                filteredPosts += allData.filter { post ->
                                    post.category == category
                                            && (post.title.contains(searchKeyword))
                                }
                                filteredPosts += allData.filter { post ->
                                    post.category == category
                                            && (post.detail.contains(searchKeyword))
                                }
                            }
                        }
                    }
                } else if (categories.isEmpty() && searchKeyword.isNotBlank()) {
                    when (searchOption) {
                        "제목 검색" -> {
                            filteredPosts += allData.filter { post ->
                                post.title.contains(searchKeyword)
                            }
                        }
                        "제목+내용 검색" -> {
                            filteredPosts += allData.filter { post ->
                                post.title.contains(searchKeyword) || post.detail.contains(searchKeyword)
                            }
                        }
                    }
                } else if (categories.isNotEmpty() && searchKeyword.isBlank()) {
                    for (category in categories) {
                        filteredPosts += allData.filter { post ->
                            post.category == category
                        }
                    }
                } else {
                    filteredPosts += allPosts.value!!
                }
                _filteredPosts.value = filteredPosts
            }.onFailure {
                Log.e(TAG, "getFilteredPosts() failed! : ${it.message}")
                handleException(it)
            }
        }
    }


    fun addPost(post: Post, callback: PostCallback) {
        viewModelScope.launch {
            runCatching {
                val newPost = post
                db.collection("post")
                    .document(newPost.id)
                    .set(newPost)
                    .await()
                callback.onSuccess()
            }.onFailure {
                Log.e(TAG, "addPost() failed! : ${it.message}")
                handleException(it)
                callback.onFailure(it)
            }
        }
    }


    fun editPost(post: Post) {
        viewModelScope.launch {
            runCatching {
                val edittedPost = post
                db.collection("post")
                    .document(post.id)
                    .set(post)
            }.onFailure {
                Log.e(TAG, "addPost() failed! : ${it.message}")
                handleException(it)
            }
        }
    }

    fun addComment(clickedItemId: String, newComment: Comments) {
        viewModelScope.launch {
            runCatching {
                db.collection("post")
                    .document(clickedItemId)
                    .update("comments", FieldValue.arrayUnion(newComment))
                    .addOnSuccessListener {
                        println("Post: ${clickedItemId}에 Comment 추가 성공")
                    }
                    .addOnFailureListener { exception ->
                        println("Post: ${clickedItemId}에 Comment 추가 실패 / $exception")
                    }
            }.onFailure {
                Log.e(TAG, "addComment() failed! : ${it.message}")
                handleException(it)
            }
        }
    }

    fun sendReport(newReport: Report) {
        viewModelScope.launch {
            runCatching {
                db.collection("report")
                    .document(generateRandomUUID())
                    .set(newReport)
                    .addOnSuccessListener {
                        println("Report: ${newReport.id}에 Report 추가 성공")
                    }
                    .addOnFailureListener { exception ->
                        println("Report: ${newReport.id}에 Report 추가 실패 / $exception")
                    }
            }.onFailure {
                Log.e(TAG, "sendReport() failed! : ${it.message}")
                handleException(it)
            }
        }
    }

    fun addReply(clickedItemId: String, clickedCommentId: String, newReply: Reply) {
        viewModelScope.launch {
            runCatching {
                val postRef = db.collection("post").document(clickedItemId)
                // Firestore 트랜잭션을 사용하여 동시에 여러 필드를 안전하게 업데이트
                db.runTransaction { transaction ->
                    val postSnapshot = transaction.get(postRef)
                    val post = postSnapshot.toObject(Post::class.java)
                    val comments = post?.comments ?: emptyList()

                    val commentIndex = comments.indexOfFirst { it.id == clickedCommentId }
                    if (commentIndex != -1) {
                        val updatedReplies = comments[commentIndex].reply + newReply
                        val updatedComment = comments[commentIndex].copy(reply = updatedReplies)
                        val updatedComments = comments.toMutableList()
                        updatedComments[commentIndex] = updatedComment
                        transaction.update(postRef, "comments", updatedComments)
                    }
                }.addOnSuccessListener {
                    println("Reply added successfully")
                }.addOnFailureListener { exception ->
                    println("Failed to add reply: $exception")
                }
            }.onFailure {
                Log.e(TAG, "addReply() failed! : ${it.message}")
                handleException(it)
            }
        }
    }

    fun deletePost(clickedItemId: String) {
        viewModelScope.launch {
            runCatching {
                db.collection("post")
                    .document(clickedItemId)
                    .delete()
                .addOnSuccessListener {
                    println("Post ${clickedItemId} deleted successfully")
                }.addOnFailureListener { exception ->
                    println("Failed to delete post : $exception")
                }
            }.onFailure {
                Log.e(TAG, "deletePost() failed! : ${it.message}")
                handleException(it)
            }
        }
    }

    fun deleteComment(clickedItemId: String, commentToDelete: Comments, callback: DeleteCommentCallback) {
        viewModelScope.launch {
            runCatching {
                val postRef = db.collection("post").document(clickedItemId)
                db.runTransaction { transaction ->
                    val postSnapshot = transaction.get(postRef)
                    val post = postSnapshot.toObject(Post::class.java)
                    val comments = post?.comments ?: emptyList()

                    val commentIndex = comments.indexOfFirst { it.id == commentToDelete.id }
                    if (commentIndex != -1) {
                        val updatedComments = comments.toMutableList()
                        updatedComments.removeAt(commentIndex)
                        transaction.update(postRef, "comments", updatedComments)
                    }
                }.addOnSuccessListener {
                    callback.onSuccess()
                }.addOnFailureListener { exception ->
                    callback.onFailure(exception)  // 콜백의 onFailure 호출
                }
            }.onFailure {
                Log.e(TAG, "deleteComment() failed! : ${it.message}")
                handleException(it)
                callback.onFailure(it)  // 콜백의 onFailure 호출
            }
        }
    }
    fun deleteReply(clickedItemId: String, clickedCommentId: String, clickedReplyId: String) {
        viewModelScope.launch {
            runCatching {
                val postRef = db.collection("post").document(clickedItemId)
                db.runTransaction { transaction ->
                    val postSnapshot = transaction.get(postRef)
                    val post = postSnapshot.toObject(Post::class.java)
                    val comments = post?.comments ?: emptyList()
                    val commentIndex = comments.indexOfFirst { it.id == clickedCommentId }
                    val replies = comments[commentIndex].reply
                    val replyIndex = replies.indexOfFirst { it.id == clickedReplyId }
                    if (replyIndex != -1) {
                        val updatedComments = comments.toMutableList()
                        val updatedReplies = updatedComments[commentIndex].reply.toMutableList()
                        updatedReplies.removeAt(replyIndex)
                        updatedComments[commentIndex].reply = updatedReplies
                        transaction.update(postRef, "comments", updatedComments)
                    }
                }.addOnSuccessListener {
                    println("답글 삭제 성공")
                }
                    .addOnFailureListener { exception ->
                        println("답글 삭제 실패 / $exception")
                    }
            }.onFailure {
                Log.e(TAG, "deleteComment() failed! : ${it.message}")
                handleException(it)
            }
        }
    }

    fun saveUserPhotoUrl(photoUrl: String) {
        db.collection("user").document(currentUser.value!!.email)
            .update("photo", photoUrl)
    }

    fun initializeTemporaryImageUrls() {
        viewModelScope.launch {
            runCatching {
                _temporaryImageUrls.value = mutableListOf()
            }.onFailure {
                Log.e(TAG, "setCategories() failed! : ${it.message}")
                handleException(it)
            }
        }
    }


    fun saveTemporaryImageUrl(imageUrl: String) {
        viewModelScope.launch {
            runCatching {
                _temporaryImageUrls.value = _temporaryImageUrls.value?.plus(imageUrl)
            }.onFailure {
                Log.e(TAG, "setCategories() failed! : ${it.message}")
                handleException(it)
            }
        }
    }

//    fun saveImageUrlToFirestore() {
//        db.collection("post").document()
//
//        // Assuming you want to save URLs in a list
//        userDocRef.update("imageUrls", FieldValue.arrayUnion(imageUrl))
//            .addOnSuccessListener {
//                println("Image URL successfully added to Firestore")
//            }
//            .addOnFailureListener { exception ->
//                println("Failed to add Image URL to Firestore: $exception")
//            }
//    }

    fun getPosterPhotoUrl(clickedItemId: String, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("post").document(clickedItemId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val downloadUrl = document.getString("posterPhoto")
                    if (downloadUrl != null) {
                        onSuccess(downloadUrl)
                    } else {
                        onFailure(Exception("Download URL not found"))
                    }
                } else {
                    onFailure(Exception("No such document"))
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun getCommenterPhotoUrl(clickedItemId: String, clickedCommentId: String,
                onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("post").document(clickedItemId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val downloadUrl = document.getString("posterPhoto")
                    if (downloadUrl != null) {
                        onSuccess(downloadUrl)
                    } else {
                        onFailure(Exception("Download URL not found"))
                    }
                } else {
                    onFailure(Exception("No such document"))
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun updateCurrentPostInfo(post: Post) {
        viewModelScope.launch {
            runCatching {
                val updatedPostInfo = post
                    db.collection("post").document(post.id)
                        .set(updatedPostInfo)
            }.onFailure {
                Log.e(TAG, "updateCurrentUserInfo() failed! : ${it.message}")
                handleException(it)
            }
        }
    }


    private fun handleException(e: Throwable) {
        when (e) {
            is HttpException -> {
                val errorJsonString = e.response()?.errorBody()?.string()
                Log.e(TAG, "HTTP error: $errorJsonString")
            }

            is IOException -> Log.e(TAG, "Network error: $e")
            else -> Log.e(TAG, "Unexpected error: $e")
        }
    }

    private fun generateRandomUUID(): String {
        return UUID.randomUUID().toString()
    }
}