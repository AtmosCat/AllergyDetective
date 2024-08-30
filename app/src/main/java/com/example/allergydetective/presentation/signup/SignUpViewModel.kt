package com.example.allergydetective.presentation.signup
import android.content.ContentValues
import android.graphics.Insets.add
import android.os.UserManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allergydetective.data.model.user.GroupMember
import com.example.allergydetective.presentation.base.UiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class SignUpViewModel() : ViewModel() {

    // 이 뷰모델이 이 프래그먼트 안에서 관찰하고 업데이트해야 하는 데이터를 LiveData의 파라미터로 설정
    // FilterFragment에서는 List<GroupMember>를 관찰해야 함
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