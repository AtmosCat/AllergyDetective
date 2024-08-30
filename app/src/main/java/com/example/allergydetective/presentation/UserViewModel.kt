package com.example.allergydetective.presentation

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allergydetective.data.model.user.User
import com.example.allergydetective.presentation.base.UiState
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class UserViewModel () : ViewModel() {

    private val _uiState: MutableLiveData<UiState<Any>> = MutableLiveData()
    val uiState: LiveData<UiState<Any>> = _uiState

    private val _users = MutableLiveData<MutableList<User>>()
    val users : LiveData<MutableList<User>> get() = _users

    private val _user = MutableLiveData<User?>()
    val user : LiveData<User?> get() = _user

    fun setUser(user: User) {
//        _uiState.value = UiState.Loading
        viewModelScope.launch {
            runCatching {
                val newUsers = _users.value?: mutableListOf()
                newUsers.add(user)
                _users.value = newUsers
                // 보통은 변수로 LiveData.value 에 새로운 값을 써줌

//                _uiState.value = UiState.Success(_homeFoods)
            }.onFailure {
                Log.e(TAG, "setUser() failed! : ${it.message}")
                handleException(it)
//                _uiState.value = UiState.Error("Error")
            }
        }
    }

    fun getUser(_id: String) {
//        _uiState.value = UiState.Loading
        viewModelScope.launch {
            runCatching {
                val currentUser =  _users.value?.find { it.id == _id }
                _user.value = currentUser
//                _uiState.value = UiState.Success(_homeFoods)
            }.onFailure {
                Log.e(TAG, "getHomeFoods() failed! : ${it.message}")
                handleException(it)
//                _uiState.value = UiState.Error("Error")
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
}