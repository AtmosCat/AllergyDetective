package com.allergyguardian.allergyguardian.presentation

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.allergyguardian.allergyguardian.data.model.food.Food
import com.allergyguardian.allergyguardian.data.model.market.Market
import com.allergyguardian.allergyguardian.data.model.user.Post
import com.allergyguardian.allergyguardian.data.repository.food.FoodRepository
import com.allergyguardian.allergyguardian.data.repository.market.MarketRepository
import com.allergyguardian.allergyguardian.network.food.RetrofitClient
import com.allergyguardian.allergyguardian.presentation.base.UiState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class SharedViewModel (private val foodRepository: FoodRepository, private val marketRepository: MarketRepository) : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _uiState: MutableLiveData<UiState<Any>> = MutableLiveData()
    val uiState: LiveData<UiState<Any>> = _uiState

    private val allergyList: List<String> = listOf("알류","계란","우유","메밀","땅콩","대두","밀","고등어","게","새우",
        "돼지고기","복숭아","토마토","아황산류","이산화황","호두","닭고기","쇠고기","소고기","오징어","잣",
        "조개류","조개","굴","전복","홍합","조개류(굴)","조개류(전복)","조개류(홍합)")

    private val _searchKeyword = MutableLiveData<String>()
    val searchKeyword : LiveData<String> get() = _searchKeyword

    private val _selectedCategories = MutableLiveData<MutableList<String>>()
    val selectedCategories : LiveData<MutableList<String>> get() = _selectedCategories

    private val _selectedAllergies = MutableLiveData<MutableList<String>>()
    val selectedAllergies : LiveData<MutableList<String>> get() = _selectedAllergies

    private val _filteredFoods = MutableLiveData<List<Food>>()
    val filteredFoods : LiveData<List<Food>> get() = _filteredFoods

    private val _totalFoods = MutableLiveData<List<Food>>()
    val totalFoods : LiveData<List<Food>> get() = _totalFoods

    private val _totalFoodsFromDB = MutableLiveData<List<Food>>()
    val totalFoodsFromDB : LiveData<List<Food>> get() = _totalFoodsFromDB

    private val _marketData = MutableLiveData<List<Market>>()
    val marketData : LiveData<List<Market>> get() = _marketData

    private val categoryNameList = listOf(
        "과자/간식류",
        "즉석식품류",
        "음료/커피류",
        "라면/면류",
        "빙과류",
        "빵/쿠키류",
        "유제품류",
        "육가공품류",
        "양념/소스류",
        "조미료류",
        "잼/시럽류",
        "기타"
        )

    private val snackCategoryKeywords = listOf("과자","캔디","사탕","빵","초콜릿","초콜렛","떡","젤리","스낵","시리얼","껌","견과류","젤리")
    private val instantFoodCategoryKeywords = listOf("즉석","냉동","만두","튀김","용기","가열","라면","유탕면","건면")
    private val beverageCoffeeCategoryKeywords = listOf("커피","음료","탄산","과채","과.채","주스","액상","다류","가공유","우유","요구르트")
    private val ramenNoodleCategoryKeywords = listOf("유탕면","건면","라면","용기면","면류","국수","숙면","냉면")
    private val frozenDessertCategoryKeywords = listOf("빙과","아이스","샤베트","슬러시","스무디","파르페","프라페")
    private val breadCookieCategoryKeywords = listOf("빵","쿠키","케익","케이크","케잌","비스킷","파이","페이스트리","페스츄리","마들렌","브라우니","크래커","웨이퍼","스콘","머핀","슈크림")
    private val dairyProductsCategoryKeywords = listOf("유제품","우유","가공유","아이스","유산균","밀크","발효유","치즈","크림","요구르트","조제유","버터","분유")
    private val processedMeatCategoryKeywords = listOf("육가공품","햄","소시지","스팸","양념육","절임육","가공육","포장육","고기","식육")
    private val seasoningSauceCategoryKeywords = listOf("양념","소스","젓갈","초장","드레싱","케첩","스파이스","요리장")
    private val seasoningAgentsCategoryKeywords = listOf("조미","미원","소금","고추장","된장","간장","혼합장","식초","향신료","식용유","가루")
    private val jamSyrupCategoryKeywords = listOf("잼","시럽","피넛버터","꿀","액상과당","스프레드")
    private val elseCategoryKeywords = listOf("")

    private val categoryKeywordsList = listOf(
        snackCategoryKeywords,
        instantFoodCategoryKeywords,
        beverageCoffeeCategoryKeywords,
        ramenNoodleCategoryKeywords,
        frozenDessertCategoryKeywords,
        breadCookieCategoryKeywords,
        dairyProductsCategoryKeywords,
        processedMeatCategoryKeywords,
        seasoningSauceCategoryKeywords,
        seasoningAgentsCategoryKeywords,
        jamSyrupCategoryKeywords
        ,elseCategoryKeywords
    )

    private val allergyNameList = listOf(
        "알류(가금류)","우유","메밀","땅콩","대두","밀","고등어","게","새우","돼지고기","복숭아","토마토","아황산류",
        "호두","닭고기","쇠고기","오징어","조개류(조개)","잣","조개류(굴)","조개류(전복)","조개류(홍합)")

    private val eggKeywords = listOf("알류","계란")
    private val milkKeywords = listOf("우유")
    private val buckwheatKeywords = listOf("메밀")
    private val peanutKeywords = listOf("땅콩")
    private val soybeanKeywords = listOf("대두")
    private val wheatKeywords = listOf("밀")
    private val mackerelKeywords = listOf("고등어")
    private val crabKeywords = listOf("게")
    private val shrimpKeywords = listOf("새우")
    private val porkKeywords = listOf("돼지")
    private val peachKeywords = listOf("복숭아")
    private val tomatoKeywords = listOf("토마토")
    private val sulfurousAcidsKeywords = listOf("아황산","이산화황")
    private val walnutKeywords = listOf("호두")
    private val chickenKeywords = listOf("닭")
    private val beefKeywords = listOf("소고기","쇠고기")
    private val squidKeywords = listOf("오징어")
    private val seashellKeywords = listOf("조개")
    private val pinenutKeywords = listOf("잣")
    private val oysterKeywords = listOf("굴")
    private val abaloneKeywords = listOf("전복")
    private val musselKeywords = listOf("홍합")

    private val allergyKeywordsList = listOf(
        eggKeywords,
        milkKeywords,
        buckwheatKeywords,
        peanutKeywords,
        soybeanKeywords,
        wheatKeywords,
        mackerelKeywords,
        crabKeywords,
        shrimpKeywords,
        porkKeywords,
        peachKeywords,
        tomatoKeywords,
        sulfurousAcidsKeywords,
        walnutKeywords,
        chickenKeywords,
        beefKeywords,
        squidKeywords,
        seashellKeywords,
        pinenutKeywords,
        oysterKeywords,
        abaloneKeywords,
        musselKeywords
        )

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

    fun setCategoryFilter(categories: MutableList<String>) {
        viewModelScope.launch {
            runCatching {
                _selectedCategories.value = categories
            }.onFailure {
                Log.e(TAG, "setCategoryFilter() failed! : ${it.message}")
                handleException(it)
            }
        }
    }

    fun setAllergyFilter(allergies: MutableList<String>) {
        viewModelScope.launch {
            runCatching {
                _selectedAllergies.value = allergies
            }.onFailure {
                Log.e(TAG, "setAllergyFilter() failed! : ${it.message}")
                handleException(it)
            }
        }
    }

    fun getAllFoods() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            runCatching {
                var data: List<Food>
                var allData = mutableListOf<Food>()
                val dataCount = RetrofitClient.gonggongFoodAPI.getGonggongFood(pageNo = "1",
                    numOfRows = "100").body?.totalCount?.toInt()
//                val maxPageNum = dataCount!!.div(100)
                val maxPageNum = 5
                for (i in 1..maxPageNum) {
                    data = foodRepository.getAllData(i)
                    allData += data
                }
                allData = allData.filter { it.allergy != "알수없음" }.toMutableList()
                _totalFoods.value = allData
                _uiState.value = UiState.Success("Example")
            }.onFailure {
                Log.e(TAG, "getAllFoods() failed! : ${it.message}")
                handleException(it)
                _uiState.value = UiState.Error("getAllFoods() failed!")
            }
        }
    }

     fun getFilteredFoods2() {
        viewModelScope.launch {
            runCatching {
                var filteredData = _totalFoods.value
                val categories = selectedCategories.value
                val allergies = selectedAllergies.value

                // 1차로 알수없음 거르기
                var filteredFoodsByCategory = emptyList<Food>()

                // 카테고리 & 검색어 필터링
                if (categories!!.isNotEmpty()) {
                    for (category in categories) {
                        val index = categoryNameList.indexOf(category)
                        for (keyword in categoryKeywordsList[index]) {
                            filteredFoodsByCategory += filteredData!!.filter { food ->
                                food.prdkind!!.contains(keyword)
                                && food.prdlstNm!!.contains(searchKeyword.value.toString())
                            }
                        }
                    }
                } else {
                    filteredFoodsByCategory = filteredData!!.filter { food ->
                        food.prdlstNm!!.contains(searchKeyword.value.toString())
                    }
                }

                var filteredFoodsByCategoryAndAllergy = emptyList<Food>()

                // 알러지 필터링
                if (allergies!!.isNotEmpty()) {
                    for (allergy in allergies) {
                        val index = allergyNameList.indexOf(allergy)
                        for (keyword in allergyKeywordsList[index]) {
                            filteredFoodsByCategoryAndAllergy = filteredFoodsByCategory.filter { food ->
                                !food.allergy!!.contains(keyword)
                                && !food.allergy.contains("알수없음")
                            }
                        }
                    }
                } else {
                    filteredFoodsByCategoryAndAllergy = filteredFoodsByCategory!!.filter { food ->
                        !food.allergy!!.contains("알수없음")
                    }
                }

                _filteredFoods.value = filteredFoodsByCategoryAndAllergy
            }.onFailure {
                Log.e(TAG, "getFilteredFoods2() failed! : ${it.message}")
                handleException(it)
            }
        }
    }

    fun getMarketDetail(manufacture: String, name: String) {
        viewModelScope.launch {
            runCatching {
                var marketResults = marketRepository.getMarketData(manufacture+" "+name)
                if (marketResults.isEmpty()) {
                    marketResults = marketRepository.getMarketData(name)
                    _marketData.value = marketResults
                } else {
                    _marketData.value = marketResults
                }
            }.onFailure {
                Log.e(TAG, "getMarketDetail() failed! : ${it.message}")
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
}