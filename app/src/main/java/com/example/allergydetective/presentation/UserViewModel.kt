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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.allergydetective.data.model.food.Food
import com.example.allergydetective.data.model.user.GroupMember
import com.example.allergydetective.data.model.user.Post
import com.example.allergydetective.data.model.user.User
import com.example.allergydetective.data.model.user.sampleBitmap
import com.example.allergydetective.presentation.base.UiState
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.ByteArrayOutputStream
import java.io.IOException

class UserViewModel (application: Application) : AndroidViewModel(application) {

    private val db = FirebaseFirestore.getInstance()

    private val _uiState: MutableLiveData<UiState<Any>> = MutableLiveData()
    val uiState: LiveData<UiState<Any>> = _uiState

    private val _users = MutableLiveData<MutableList<User>>()
    val users : LiveData<MutableList<User>> get() = _users

    private val _signingInUser = MutableLiveData<User?>()
    val signingInUser : LiveData<User?> get() = _signingInUser

    private val _currentUser = MutableLiveData<User?>()
    val currentUser : LiveData<User?> get() = _currentUser

    private val _currentUserAllergies = MutableLiveData<MutableList<String>?>()
    val currentUserAllergies : LiveData<MutableList<String>?> get() = _currentUserAllergies

    private val _currentUserFavorites = MutableLiveData<MutableList<Food>?>()
    val currentUserFavorites : LiveData<MutableList<Food>?> get() = _currentUserFavorites

    private val _bitmapBeforeSave = MutableLiveData<Bitmap>()
    val bitmapBeforeSave : LiveData<Bitmap> get() = _bitmapBeforeSave

    fun addUser(user: User) {
        viewModelScope.launch {
            runCatching {
                db.collection("user")
                    .document(user.email)
                    .set(user)

            }.onFailure {
                Log.e(TAG, "addUser() failed! : ${it.message}")
                handleException(it)
            }
        }
    }

    fun addMyPost(email: String, post: Post) {
        viewModelScope.launch {
            val postWithoutTimeStamp = post.copy(timestamp = null)

            runCatching {
                db.collection("user")
                    .document(email)
                    .update("mypost", FieldValue.arrayUnion(postWithoutTimeStamp))
                    .addOnSuccessListener {
                        println("CurrentUser의 MyPost에 ${post.id} 추가 성공")
                    }
                    .addOnFailureListener { exception ->
                        println("CurrentUser의 MyPost에 ${post.id} 추가 실패 / $exception")
                    }
            }.onFailure {
                Log.e(TAG, "addMyPost() failed! : ${it.message}")
                handleException(it)
            }
        }
    }

    fun findUser(_email: String) {
        viewModelScope.launch {
            runCatching {
                db.collection("user")
                    .whereEqualTo("email", _email)
                    .get()
                    .addOnSuccessListener { result ->
                        if (result != null) {
                            for (document in result) {
                                val user = document.toObject(User::class.java)
                                _signingInUser.value = user
                            }
                        } else {
                            _signingInUser.value = null
                        }
                    }
                    .addOnFailureListener{
                        _signingInUser.value = null
                    }
            }.onFailure {
                Log.e(TAG, "findUser() failed! : ${it.message}")
                handleException(it)
            }
        }
    }

    fun setCurrentUser(_email: String) {
        viewModelScope.launch {
            runCatching {
                db.collection("user")
                    .whereEqualTo("email", _email)
                    .get()
                    .addOnSuccessListener { result ->
                        if (result != null) {
                            for (document in result) {
                                val user = document.toObject(User::class.java)
                                _currentUser.value = user
                            }
                        } else {
                            _currentUser.value = null
                        }
                    }
                    .addOnFailureListener {
                        _currentUser.value = null
                    }
            }.onFailure {
                Log.e(TAG, "setCurrentUser() failed! : ${it.message}")
                handleException(it)
            }
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

    fun updateGroup(newGroup: MutableList<GroupMember>) {
        viewModelScope.launch {
            runCatching {
                db.collection("user").document(currentUser.value!!.email)
                    .update("group", newGroup)
            }.onFailure {
                Log.e(TAG, "updateGroup() failed! : ${it.message}")
                handleException(it)
            }
        }
    }

    // 갤러리로부터 이미지 값을 받아와서 Bitmap 으로 변환
    suspend fun handleImage(uri: Uri) {
        _bitmapBeforeSave.value = sampleBitmap
        val imageLoader = ImageLoader(getApplication())
        val request = ImageRequest.Builder(getApplication())
            .data(uri)
            .allowHardware(false) // Bitmap을 요청할 때는 false로 설정
            .build()

        val result = imageLoader.execute(request)
        if (result is SuccessResult) {
            _bitmapBeforeSave.value = result.drawable.toBitmap()
        } else {
            // 실패 처리
        }
    }

    // 임시 저장하는 Bitmap 파일을 String 타입의 다운로드 URL 값으로 변환
    fun uploadImageToFirebaseStorage(onSuccess: () -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference.child("images/${System.currentTimeMillis()}.png")
        val baos = ByteArrayOutputStream()
        bitmapBeforeSave.value?.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = storageRef.putBytes(data)
        uploadTask.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                saveUserPhotoUrl(uri.toString())
                _currentUser.value?.photo = uri.toString()
                onSuccess()
            }
        }.addOnFailureListener {
            // 업로드 실패 처리
        }
    }

    fun saveUserPhotoUrl(photoUrl: String) {
        db.collection("user").document(currentUser.value!!.email)
            .update("photo", photoUrl)
    }

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
}