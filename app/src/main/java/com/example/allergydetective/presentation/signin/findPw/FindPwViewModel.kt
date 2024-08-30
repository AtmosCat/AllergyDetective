package com.example.allergydetective.presentation.signin.findPw

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allergydetective.data.model.user.GroupMember
import com.example.allergydetective.presentation.base.UiState
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class FindPwViewModel: ViewModel() {
    private val _uiState: MutableLiveData<UiState<List<GroupMember>>> = MutableLiveData()
    val uiState: LiveData<UiState<List<GroupMember>>> = _uiState

    fun setFilter(selectedMember_: List<GroupMember>) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            runCatching {
//                val selectedMember = selectedMember_
//                _uiState.value = UiState.Success(selectedMember)

            }.onFailure {
                Log.e(ContentValues.TAG, "setFilter() failed! : ${it.message}")
                handleException(it)
                _uiState.value = UiState.Error("Error")
            }
        }
    }

    private fun handleException(e: Throwable) {
        when (e) {
            is HttpException -> {
                val errorJsonString = e.response()?.errorBody()?.string()
                Log.e(ContentValues.TAG, "HTTP error: $errorJsonString")
            }

            is IOException -> Log.e(ContentValues.TAG, "Network error: $e")
            else -> Log.e(ContentValues.TAG, "Unexpected error: $e")
        }
    }
}
