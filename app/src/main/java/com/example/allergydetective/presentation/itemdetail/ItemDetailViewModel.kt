package com.example.allergydetective.presentation.itemdetail

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allergydetective.data.model.food.Food
import com.example.allergydetective.data.model.food.FoodManager
import com.example.allergydetective.data.model.market.Market
import com.example.allergydetective.data.model.market.MarketManager
import com.example.allergydetective.data.repository.food.FoodRepository
import com.example.allergydetective.data.repository.market.MarketRepository
import com.example.allergydetective.presentation.base.UiState
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ItemDetailViewModel(private val repository: MarketRepository) : ViewModel() {

    private val _uiState: MutableLiveData<UiState<List<Market>>> = MutableLiveData()
    val uiState: LiveData<UiState<List<Market>>> = _uiState

    fun getMarketDetail(name: String) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            runCatching {
                val marketResults = repository.getMarketData(name)
                MarketManager.marketResults = marketResults
                _uiState.value = UiState.Success(marketResults)

            }.onFailure {
                Log.e(ContentValues.TAG, "getMarketDetail() failed! : ${it.message}")
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