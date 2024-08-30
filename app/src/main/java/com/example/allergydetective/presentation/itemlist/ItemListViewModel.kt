//package com.example.allergydetective.presentation.itemlist
//
//import android.content.ContentValues.TAG
//import android.util.Log
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.allergydetective.data.model.food.Food
//import com.example.allergydetective.data.model.food.FoodManager
//import com.example.allergydetective.data.repository.food.FoodRepository
//import com.example.allergydetective.presentation.base.UiState
//import kotlinx.coroutines.launch
//import retrofit2.HttpException
//import java.io.IOException
//
//// 홈화면 Viewmodel
//// 균일화된 레시피를 활용하기 위해 repository: FoodRepository를 파라미터로 주입
//class ItemListViewModel(private val repository: FoodRepository) : ViewModel() {
//
//    private val _uiState: MutableLiveData<UiState<List<Food>>> = MutableLiveData()
//    val uiState: LiveData<UiState<List<Food>>> = _uiState
//
//    fun getFilteredFoods() {
//        _uiState.value = UiState.Loading
//        viewModelScope.launch {
//            runCatching {
//                val filteredFoodsByCategory = repository.getFilteredData()
//                val selectedAllergies = FoodManager.selectedAllergies
//
//                var filteredFoodsByCategoryAndAllergy: List<Food> = mutableListOf()
//
//                for (allergy in selectedAllergies) {
//                    filteredFoodsByCategoryAndAllergy = filteredFoodsByCategory.filter {
//                        !it.allergy!!.contains(allergy) && !it.allergy.contains("알수없음")}
//                    FoodManager.filteredFoods = filteredFoodsByCategoryAndAllergy
//                }
//                _uiState.value = UiState.Success(filteredFoodsByCategoryAndAllergy)
//
//            }.onFailure {
//                Log.e(TAG, "getFilteredFoods() failed! : ${it.message}")
//                handleException(it)
//                _uiState.value = UiState.Error("Error")
//            }
//        }
//    }
//
//    private fun handleException(e: Throwable) {
//        when (e) {
//            is HttpException -> {
//                val errorJsonString = e.response()?.errorBody()?.string()
//                Log.e(TAG, "HTTP error: $errorJsonString")
//            }
//
//            is IOException -> Log.e(TAG, "Network error: $e")
//            else -> Log.e(TAG, "Unexpected error: $e")
//        }
//    }
//}