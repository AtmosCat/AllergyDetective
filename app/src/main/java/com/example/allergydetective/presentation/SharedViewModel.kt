package com.example.allergydetective.presentation

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allergydetective.data.model.food.Food
import com.example.allergydetective.data.model.food.FoodManager
import com.example.allergydetective.data.model.market.Market
import com.example.allergydetective.data.model.market.MarketManager
import com.example.allergydetective.data.model.user.GroupMember
import com.example.allergydetective.data.repository.food.FoodRepository
import com.example.allergydetective.data.repository.market.MarketRepository
import com.example.allergydetective.presentation.base.UiState
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class SharedViewModel (private val foodRepository: FoodRepository, private val marketRepository: MarketRepository) : ViewModel() {

    private val _uiState: MutableLiveData<UiState<Any>> = MutableLiveData()
    val uiState: LiveData<UiState<Any>> = _uiState

    private val _foods = MutableLiveData<List<Food>>()
    val foods : LiveData<List<Food>> get() = _foods

    private val _homeFoods = MutableLiveData<List<Food>>()
    val homeFoods : LiveData<List<Food>> get() = _homeFoods

    private val allergyList: List<String> = listOf("알류","계란","우유","메밀","땅콩","대두","밀","고등어","게","새우",
        "돼지고기","복숭아","토마토","아황산류","이산화황","호두","닭고기","쇠고기","소고기","오징어","잣",
        "조개류","조개","굴","전복","홍합","조개류(굴)","조개류(전복)","조개류(홍합)")

    private val _searchKeyword = MutableLiveData<String>()
    val searchKeyword : LiveData<String> get() = _searchKeyword

    private val _selectedCategory = MutableLiveData<String>()
    val selectedCategory : LiveData<String> get() = _selectedCategory

    private val _selectedAllergies = MutableLiveData<MutableList<String>>()
    val selectedAllergies : LiveData<MutableList<String>> get() = _selectedAllergies

    private val _selectedAllergiesCount = MutableLiveData<Int>()
    val selectedAllergiesCount : LiveData<Int> get() = _selectedAllergiesCount

    private val _filteredFoods = MutableLiveData<List<Food>>()
    val filteredFoods : LiveData<List<Food>> get() = _filteredFoods

    private val _marketData = MutableLiveData<List<Market>>()
    val marketData : LiveData<List<Market>> get() = _marketData

    fun getHomeFoods() {
//        _uiState.value = UiState.Loading
        viewModelScope.launch {
            runCatching {
                _homeFoods.value = foodRepository.getList()
//                _uiState.value = UiState.Success(_homeFoods)
            }.onFailure {
                Log.e(TAG, "getHomeFoods() failed! : ${it.message}")
                handleException(it)
//                _uiState.value = UiState.Error("Error")
            }
        }
    }

    fun setSearchKeyword(keyword: String) {
//        _uiState.value = UiState.Loading
        viewModelScope.launch {
            runCatching {
                _searchKeyword.value = keyword
//                _uiState.value = UiState.Success("Example")
            }.onFailure {
                Log.e(TAG, "setSearchKeyword() failed! : ${it.message}")
                handleException(it)
//                _uiState.value = UiState.Error("Error")
            }
        }
    }

    fun setCategoryFilter(category: String) {
//        _uiState.value = UiState.Loading
        viewModelScope.launch {
            runCatching {
                _selectedCategory.value = category
//                _uiState.value = UiState.Success("Example")

            }.onFailure {
                Log.e(TAG, "setCategoryFilter() failed! : ${it.message}")
                handleException(it)
//                _uiState.value = UiState.Error("Error")
            }
        }
    }

    fun addAllergiesFilter(allergen : String) {
//        _uiState.value = UiState.Loading
        viewModelScope.launch {
            runCatching {
                val newSelectedAllergies: MutableList<String> = _selectedAllergies.value?: mutableListOf()
                newSelectedAllergies.add(allergen)
                _selectedAllergies.value = newSelectedAllergies
//                _uiState.value = UiState.Success("Example")

            }.onFailure {
                Log.e(TAG, "setAllergiesFilter() failed! : ${it.message}")
                handleException(it)
//                _uiState.value = UiState.Error("Error")
            }
        }
    }

    fun removeAllergiesFilter(allergen : String) {
//        _uiState.value = UiState.Loading
        viewModelScope.launch {
            runCatching {
                val newSelectedAllergies: MutableList<String> = _selectedAllergies.value?: mutableListOf()
                newSelectedAllergies.remove(allergen)
                _selectedAllergies.value = newSelectedAllergies
//                _uiState.value = UiState.Success("Example")

            }.onFailure {
                Log.e(TAG, "setAllergiesFilter() failed! : ${it.message}")
                handleException(it)
//                _uiState.value = UiState.Error("Error")
            }
        }
    }

    fun setAllergiesCount(num: Int){
//        _uiState.value = UiState.Loading
        viewModelScope.launch {
            runCatching {
                _selectedAllergiesCount.value = num
//                _uiState.value = UiState.Success("Example")
            }.onFailure {
                Log.e(TAG, "setAllergiesCount() failed! : ${it.message}")
                handleException(it)
//                _uiState.value = UiState.Error("Error")
            }
        }
    }

    fun setFilter(selectedMember_: List<GroupMember>) {
//        _uiState.value = UiState.Loading
        viewModelScope.launch {
            runCatching {
                val selectedMember = selectedMember_
//                _uiState.value = UiState.Success(selectedMember)

            }.onFailure {
                Log.e(TAG, "setFilter() failed! : ${it.message}")
                handleException(it)
//                _uiState.value = UiState.Error("Error")
            }
        }
    }

    fun getFilteredFoods() {
//        _uiState.value = UiState.Loading
        viewModelScope.launch {
            runCatching {
                var filteredFoodsByCategory = foodRepository.getFilteredData(selectedCategory.value!!)
                val selectedAllergiesList = selectedAllergies.value
                // 1차로 알수없음 거르기

                var filteredFoodsByCategoryAndAllergy = emptyList<Food>()

                // 알러지 필터 0개 설정 시
                if (selectedAllergiesList?.size == 0) {
                    filteredFoodsByCategoryAndAllergy = filteredFoodsByCategory.filter { food ->
                        !food.allergy!!.contains("알수없음")
                        && food.prdlstNm!!.contains(searchKeyword.value.toString())
                    }
                }
                // 알러지 필터 1개 설정 시
                else if (selectedAllergiesList?.size == 1) {
                    filteredFoodsByCategoryAndAllergy = filteredFoodsByCategory.filter { food ->
                        !food.allergy!!.contains("알수없음")
                        && food.prdlstNm!!.contains(searchKeyword.value.toString())
                        && !food.allergy.contains(selectedAllergiesList[0])
                    }
                }
                // 알러지 필터 2개 설정 시
                else if (selectedAllergiesList?.size == 2) {
                    filteredFoodsByCategoryAndAllergy = filteredFoodsByCategory.filter { food ->
                        !food.allergy!!.contains("알수없음")
                        && food.prdlstNm!!.contains(searchKeyword.value.toString())
                        && !food.allergy.contains(selectedAllergiesList[0])
                        && !food.allergy.contains(selectedAllergiesList[1])
                    }
                }
                _filteredFoods.value = filteredFoodsByCategoryAndAllergy
//                _uiState.value = UiState.Success(filteredFoodsByCategoryAndAllergy)
            }.onFailure {
                Log.e(TAG, "getFilteredFoods() failed! : ${it.message}")
                handleException(it)
//                _uiState.value = UiState.Error("Error")
            }
        }
    }

    fun getMarketDetail(name: String) {
//        _uiState.value = UiState.Loading
        viewModelScope.launch {
            runCatching {

                val marketResults = marketRepository.getMarketData(name)
                _marketData.value = marketResults
//                _uiState.value = UiState.Success(marketResults)

            }.onFailure {
                Log.e(ContentValues.TAG, "getMarketDetail() failed! : ${it.message}")
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