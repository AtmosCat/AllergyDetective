package com.example.allergydetective.presentation.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allergydetective.data.model.food.Food
import com.example.allergydetective.data.model.food.FoodManager
import com.example.allergydetective.data.repository.food.FoodRepository
import com.example.allergydetective.presentation.base.UiState
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

// 홈화면 Viewmodel
// 균일화된 레시피를 활용하기 위해 repository: FoodRepository를 파라미터로 주입
class HomeViewModel(private val repository: FoodRepository) : ViewModel() {

    private val _uiState: MutableLiveData<UiState<List<Food>>> = MutableLiveData()
    val uiState: LiveData<UiState<List<Food>>> = _uiState

    fun fetchFoods() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            runCatching {
                val foodItems = repository.getList()
                FoodManager.foods = foodItems
                _uiState.value = UiState.Success(foodItems)

            }.onFailure {
                Log.e(TAG, "fetchFoods() failed! : ${it.message}")
                handleException(it)
                _uiState.value = UiState.Error("Error")
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