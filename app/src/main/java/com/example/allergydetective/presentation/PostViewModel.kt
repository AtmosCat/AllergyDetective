package com.example.allergydetective.presentation

import android.app.Application
import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.allergydetective.data.model.food.Food
import com.example.allergydetective.data.model.user.Comments
import com.example.allergydetective.data.model.user.GroupMember
import com.example.allergydetective.data.model.user.Post
import com.example.allergydetective.data.model.user.Reply
import com.example.allergydetective.data.model.user.User
import com.example.allergydetective.data.model.user.sampleBitmap
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import org.w3c.dom.Comment
import retrofit2.HttpException
import java.io.ByteArrayOutputStream
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
        viewModelScope.launch {
            runCatching {
                db.collection("post")
                    .get()
                    .addOnSuccessListener { result ->
                        if (!result.isEmpty) {
                            val posts = mutableListOf<Post>()
                            for (document in result) {
                                val post =
                                    document.toObject(Post::class.java).copy(id = document.id)
                                posts.add(post)
                            }
                            _allPosts.postValue(posts)
                        } else {
                            // 예외처리
                        }
                    }
                    .addOnFailureListener { exception ->
                        println("문서 가져오기 실패: $exception")
                    }
            }.onFailure {
                Log.e(TAG, "getAllPosts() failed! : ${it.message}")
                handleException(it)
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
                val allData = allPosts.value
                val categories = selectedCategories.value
                val searchOption = selectedSearchOption.value
                val searchKeyword = searchKeyword.value

                var filteredPosts = mutableListOf<Post>()

                for (category in categories!!) {
                    when (searchOption) {
                        "제목 검색" -> {
                            filteredPosts += allData!!.filter { post ->
                                post.category == category
                                        && post.title.contains(searchKeyword.toString())
                            }
                        }
                        "제목+내용 검색" -> {
                            filteredPosts += allData!!.filter { post ->
                                post.category == category
                                        && post.title.contains(searchKeyword.toString())
                            }
                            filteredPosts += allData!!.filter { post ->
                                post.category == category
                                        && post.detail.contains(searchKeyword.toString())
                            }
                        }
                    }
                }

                _filteredPosts.postValue(filteredPosts)
            }.onFailure {
                Log.e(TAG, "getFilteredPosts() failed! : ${it.message}")
                handleException(it)
            }
        }
    }

    fun addPost(category: String, title: String, detail: String) {
        viewModelScope.launch {
            runCatching {
                val newPost = Post(
                    id = generateRandomUUID(),
                    category = category,
                    posterPhoto = currentUser.value!!.photo,
                    posterName = currentUser.value!!.nickname,
                    title = title,
                    detail = detail,
                    detailPhoto = temporaryImageUrls.value!!
                )
                db.collection("post")
                    .document(newPost.id)
                    .set(newPost)
            }.onFailure {
                Log.e(TAG, "addPost() failed! : ${it.message}")
                handleException(it)
            }
        }
    }

    fun addComment(clickedItemId: String, commentDetail: String) {
        viewModelScope.launch {
            runCatching {
                val newComment = Comments(
                    id = generateRandomUUID(),
                    commenterPhoto = currentUser.value!!.photo,
                    commenterName = currentUser.value!!.nickname,
                    detail = commentDetail
                )
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
                Log.e(TAG, "addUser() failed! : ${it.message}")
                handleException(it)
            }
        }
    }

    fun addReply(clickedItemId: String, clickedCommentId: String, replyDetail: String) {
        viewModelScope.launch {
            runCatching {
                val newReply = Reply(
                    id = generateRandomUUID(),
                    replierPhoto = currentUser.value!!.photo,
                    replierName = currentUser.value!!.nickname,
                    detail = replyDetail
                )
                val updateMap = hashMapOf<String, Any>(
                    "comments" to hashMapOf(
                        clickedCommentId to hashMapOf(
                            "reply" to FieldValue.arrayUnion(newReply)
                        )
                    )
                )
                db.collection("post")
                    .document(clickedItemId)
                    .update(updateMap)
                    .addOnSuccessListener {
                        println("Comment: ${clickedCommentId}에 Comment 추가 성공")
                    }
                    .addOnFailureListener { exception ->
                        println("Comment: ${clickedCommentId}에 Comment 추가 실패 / $exception")
                    }

            }.onFailure {
                Log.e(TAG, "addReply() failed! : ${it.message}")
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

    fun getDownloadUrl(onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("user").document(currentUser.value!!.email)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val downloadUrl = document.getString("photo")
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

    fun updateCurrentUserInfo() {
        viewModelScope.launch {
            runCatching {
                val updatedUserInfo = currentUser.value
                if (updatedUserInfo != null) {
                    db.collection("user").document(currentUser.value!!.email)
                        .set(updatedUserInfo)
                }
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

    fun generateRandomUUID(): String {
        return UUID.randomUUID().toString()
    }
}