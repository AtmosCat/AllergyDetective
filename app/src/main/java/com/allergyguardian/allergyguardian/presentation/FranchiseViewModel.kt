package com.allergyguardian.allergyguardian.presentation

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.allergyguardian.allergyguardian.data.model.food.Food
import com.allergyguardian.allergyguardian.data.model.franchise.Chicken
import com.allergyguardian.allergyguardian.data.model.franchise.FastFood
import com.allergyguardian.allergyguardian.data.model.franchise.Menu
import com.allergyguardian.allergyguardian.data.model.franchise.Pizza
import com.allergyguardian.allergyguardian.data.model.market.Market
import com.allergyguardian.allergyguardian.data.model.user.Post
import com.allergyguardian.allergyguardian.network.food.RetrofitClient
import com.allergyguardian.allergyguardian.presentation.base.UiState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.HttpException
import java.io.IOException

class FranchiseViewModel(application: Application) : AndroidViewModel(application) {

    private val db = FirebaseFirestore.getInstance()

    private val _uiState: MutableLiveData<UiState<Any>> = MutableLiveData()
    val uiState: LiveData<UiState<Any>> = _uiState

    private val allergyList: List<String> = listOf("알류","계란","우유","메밀","땅콩","대두","밀","고등어","게","새우",
        "돼지고기","복숭아","토마토","아황산류","이산화황","호두","닭고기","쇠고기","소고기","오징어","잣",
        "조개류","조개","굴","전복","홍합","조개류(굴)","조개류(전복)","조개류(홍합)")

    private val _searchKeyword = MutableLiveData<String>()
    val searchKeyword : LiveData<String> get() = _searchKeyword

    private val _selectedAllergies = MutableLiveData<MutableList<String>>()
    val selectedAllergies : LiveData<MutableList<String>> get() = _selectedAllergies

    private val _filteredMenus = MutableLiveData<List<Food>>()
    val filteredMenus : LiveData<List<Food>> get() = _filteredMenus

    private val _allMenus = MutableLiveData<List<Menu>>()
    val allMenus : LiveData<List<Menu>> get() = _allMenus

    private val _fastfoodMenus = MutableLiveData<List<Menu>>()
    val fastfoodMenus : LiveData<List<Menu>> get() = _fastfoodMenus

    private val _chickenMenus = MutableLiveData<List<Menu>>()
    val chickenMenus : LiveData<List<Menu>> get() = _chickenMenus

    private val _pizzaMenus = MutableLiveData<List<Menu>>()
    val pizzaMenus : LiveData<List<Menu>> get() = pizzaMenus

    private val _cafeMenus = MutableLiveData<List<Menu>>()
    val cafeMenus : LiveData<List<Menu>> get() = _cafeMenus


    private val allergyNameList = listOf(
        "알류(가금류)","우유","메밀","땅콩","대두","밀","고등어","게","새우","돼지고기","복숭아","토마토","아황산류",
        "호두","닭고기","쇠고기","오징어","조개류(조개)","잣","조개류(굴)","조개류(전복)","조개류(홍합)")

    private val eggKeywords = listOf("알류","계란", "난류")
    private val milkKeywords = listOf("우유")
    private val buckwheatKeywords = listOf("메밀")
    private val peanutKeywords = listOf("땅콩", "견과")
    private val soybeanKeywords = listOf("대두")
    private val wheatKeywords = listOf("밀")
    private val mackerelKeywords = listOf("고등어")
    private val crabKeywords = listOf("게", "갑각")
    private val shrimpKeywords = listOf("새우", "갑각")
    private val porkKeywords = listOf("돼지")
    private val peachKeywords = listOf("복숭아")
    private val tomatoKeywords = listOf("토마토")
    private val sulfurousAcidsKeywords = listOf("아황산","이산화황")
    private val walnutKeywords = listOf("호두", "견과")
    private val chickenKeywords = listOf("닭")
    private val beefKeywords = listOf("소고기","쇠고기")
    private val squidKeywords = listOf("오징어")
    private val seashellKeywords = listOf("조개")
    private val pinenutKeywords = listOf("잣", "견과")
    private val oysterKeywords = listOf("굴", "조개")
    private val abaloneKeywords = listOf("전복", "조개")
    private val musselKeywords = listOf("홍합", "조개")

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
                Log.e(ContentValues.TAG, "setSearchKeyword() failed! : ${it.message}")
                handleException(it)
            }
        }
    }


    fun setAllergyFilter(allergies: MutableList<String>) {
        viewModelScope.launch {
            runCatching {
                _selectedAllergies.value = allergies
            }.onFailure {
                Log.e(ContentValues.TAG, "setAllergyFilter() failed! : ${it.message}")
                handleException(it)
            }
        }
    }

    fun getAllMenus() {
        val allData = mutableListOf<Menu>()
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            runCatching {
                val dbbDocumentNames = listOf(
                    "FcdGtT08Opi8yLru5gHn", // 패스트푸드
                    "YkiQthM8m219H0KrhmDe", // 피자
                    "KVgFYLHd75znBaatAkRP" // 치킨
                )

                // 비동기 결과 리스트 생성
                val menus = dbbDocumentNames.map { docId ->
                    async {
                        val document = db.collection("franchise").document(docId).get().await()
                        if (document != null) {
                            val sheet1 = document.get("Sheet1") as? List<Map<String, Any>> ?: return@async emptyList()
                            sheet1.map { data ->
                                Menu(
                                    id = data["id"] as? String ?: "",
                                    type = data["type"] as? String ?: "",
                                    brand = data["brand"] as? String ?: "",
                                    subcat = data["subcat"] as? String ?: "",
                                    name = data["name"] as? String ?: "",
                                    allergy = data["allergy"] as? String ?: "",
                                    weight = data["weight"] as? String ?: "",
                                    kcal = data["kcal"] as? String ?: "",
                                    natrium = data["natrium"] as? String ?: "",
                                    sugar = data["sugar"] as? String ?: "",
                                    fat = data["fat"] as? String ?: "",
                                    protein = data["protein"] as? String ?: "",
                                    origin = data["origin"] as? String ?: "",
                                    nutrients = data["nutrients"] as? String ?: "",
                                    imgurl = data["imgurl"] as? String ?: "",
                                    url = data["url"] as? String ?: "",
                                    date = data["date"] as? String ?: ""
                                )
                            }
                        } else {
                            emptyList()
                        }
                    }
                }

                // 모든 결과를 기다림
                val menusList = menus.awaitAll()
                menusList.forEach { allData += it }

                // 모든 메뉴가 처리된 후 UI 상태 업데이트
                _allMenus.value = allData
                _uiState.value = UiState.Success("getAllMenus() Succeeded")
            }.onFailure {
                Log.e(ContentValues.TAG, "getAllMenus() failed! : ${it.message}")
                handleException(it)
                _uiState.value = UiState.Error("getAllMenus() failed!")
            }
        }
    }


    fun updateMenu(menu: Menu) {
        viewModelScope.launch {
            runCatching {
                val newMenu = menu
                db.collection("menu")
                    .document(newMenu.brand + "_" + newMenu.name)
                    .set(newMenu)
            }.onFailure {
                Log.e(ContentValues.TAG, "updateMenu() failed! : ${it.message}")
                handleException(it)
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
























    val starbucksBeverageUrls = listOf(
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000002487",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000000479",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000002081",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000002407",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000002093",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000005282",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004312",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000000487",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000003661",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004750",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000003285",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004770",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000000038",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000001636",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000001635",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000005378", // 콜드브루 16개
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=106509",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=2", // 브루드 커피 // 2개
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000005514",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000005516",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004771",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004774",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000002259",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000005518",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000005522",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000002262",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000005520",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000005523",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=30",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=25",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=110563",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=94",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=110582",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=126197",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=110601",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=38",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004119",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000001939",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000002095",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=128692",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004120",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000001941",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=128695",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000005285",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=110569",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000005284",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=41",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000005292",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=110566",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=110572",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=46",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004313",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=128192",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000005181",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000005178",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=110612",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000002950",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000003505",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000003506",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000002953",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=20",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9900000001604",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=110611",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000001631",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=110614", // 에스프레소 43개
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000002760",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=168007",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=168016",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=168010",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000005369",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000002088",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000002502",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000002090",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=168066",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000002403", // 프라푸치노 10개
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004512",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004716",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004945",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004870",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000005377",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000003276",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=169001",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000005379",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004871",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004780", // 블렌디드 10개
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004753",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000003763",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004439", // 리프레셔 3개
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000005283",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004948",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004751",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004946",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004947",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=107051", // 피지오 6개
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000005481",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000005484",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=4004000000056",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=4004000000059",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=4004000000039",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000000229",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000002959",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=4004000000019",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=400400000094",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004944",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=4004000000079",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=4004000000069",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=4004000000036",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000000226",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000002956",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=4004000000016",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000000187",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=400400000091",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004943",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=4004000000076",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=4004000000066",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004566",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004990",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000005310",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000005307",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000005304",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004991",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004769",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000000190",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004954",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004933",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004942",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004955",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004936",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004288",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000002499",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=135612",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004285",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000002496",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=135608", // 티바나 40개
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004950",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=72",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=110621",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004953",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004952",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000003658",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000002594",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004951",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000003659",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=17",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=18",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9200000004957", // 기타 제조 음료 12개
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9300000005406",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9300000005405",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9300000004346",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9300000004347",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9300000004348",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=5210008070",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=5210008061",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9300000003771",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9300000003772",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=5210008072",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=5210008055",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9300000002565",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9300000004407",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=9300000003775",
        "https://www.starbucks.co.kr/menu/drink_view.do?product_cd=5210008063" // 병음료 15개
    )

    private val starbucksFoodUrls = listOf(
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005463",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004823",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004834",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004833",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000001557",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004028",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000001361",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004008",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000002431",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005143",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005262",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000003520",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000002489",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000002931",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=5110006070", // 브레드 15개
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005551",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005467",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000001179",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000003393",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000002929",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000001907",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004043",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005468",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005454",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=5110007181",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005312",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005472",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004518",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005231",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000002448",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005094",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000000132",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000001055",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000002857",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000003468",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000003469",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004098",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004099",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004100",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000000913",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000002065",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000001521",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000000882",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000002064", // 케이크 29개
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005471",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005470",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000000689",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004550",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004506",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005349",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005464",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005233",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005350",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000002178",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000002435",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005234",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000001927",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004337",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004311",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005157",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005046",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004460",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005220",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005219",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005134",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000001777", // 샌드위치&샐러드 22개 (오! 별의 별 샌드위치 제외)
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9900000000680",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9900000001386", // 따뜻한 푸드 2개
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004508",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000001911",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005088",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000002408",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000001912",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000002619",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000003231",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000003232",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004509",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000002620", // 과일&요거트, 10개
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005528",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005313",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005408",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004821",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=5650003321",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=5650003322",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000001610",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000001257",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005373",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005372",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=5650003319",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005237",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005315",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005037",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004942",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000001785",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=5650003320",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004134",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000001924",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=5110008078",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=5110008075",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004339",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004135",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004338",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004517",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004820",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005085",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004774",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000002517",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005034",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004784",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005153",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000001058",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000002621",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004940",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000003249",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000002556",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004773",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000003250",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000003251",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004850",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000002718",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004212",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000002000", // 스낵 & 미니 디저트 44개(풀문 사블레 쿠키 제외)
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005133",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005404",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004515",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000005403",
        "https://www.starbucks.co.kr/menu/food_view.do?product_cd=9300000004514" // 아이스크림 5개
    )

    val twosomeBevUrls = listOf(
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10191981",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10191980",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10191970",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10191489",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10191951",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10191950",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10100001",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10100002",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10100003",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10100004",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10100005",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10100007",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10100017",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10191382",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10191458",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10100019",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10100018",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10100009",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10100008",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10191504",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10100022",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10100021",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10100010", // 커피 23개
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10302284",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10201905",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10201904",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10302241",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10302240",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10302239",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10300054",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10300038",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10300036",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10301794",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10301781",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10300048",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10300046",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10300045",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10300042",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10300041",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10302165",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10301764",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10301763",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10300050",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10300049",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10300056",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10300053", // 음료 23개
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10201961",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10201897",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10201896",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10201605",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10200028",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10200027",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10200026",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10200025",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10201683",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10200032",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10200031",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10200030",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10200029",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10200079",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10200035",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10200034",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10200033",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=10200024", // 티 18개
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=20501750",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=20501690",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=20600133", // 아이스크림빙수 3개
    )

    val twosomeWholeCakeUrls = listOf(
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30702227",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30702210",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30702268",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30702243",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30701708",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30701710",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30702000",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30702235",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30702060",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30702062",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30702058",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30701694",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30701808",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30702197",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30702053",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30702105",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30702107",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30702146",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30701891",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30701799",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30701812",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30701802",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30702239",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30702154",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30702195",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30701971",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30701747",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30701801",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30702097",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30701889",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30701769",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30702236",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30701705",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30702212",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30702241",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30701736",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30701758",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30702001",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30701703",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30701785" // 홀케이크 40개
    )

    val twosomeFoodUrls = listOf(
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30801963",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30801958",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30801704",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30801940",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30801956",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30801952",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30801735",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30801702",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30801784",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30801933",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30801718",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30801748",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30801809",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30801868",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30801786",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30801806",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30801954",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30801912",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30801869",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30801904",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30801807",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30901737",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30901728", // 피스케이크 23개
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30901810",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30901811",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30901853",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30901854",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30901825",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30901822",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30901824",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30901823", // 마카롱 8개
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=30901823",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=41001883",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=41001878",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=41001867",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=41001866",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=41001880",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=41001882",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=41001881",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=41001871",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=41001870",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=41001879",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=41001876",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=41001875",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=41001837",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=41001838",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=41001834",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=41001865",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=41001848",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=41001847", // 샌드위치 19개
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=41101902",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=41101901",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=41301878",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=41391034",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=41901976", // 샐러드기타 5개
//        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=61791029", 예외 정보 뺌
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=61791028",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=61791027",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=61791026",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=51491032",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=51491033",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=51491034",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=51491031",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=61902045",
        "https://mo.twosome.co.kr/mn/menuInfoList.do?grtCd=3&pMidCd=12",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=51501962",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=51501961",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=51491028",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=51491003",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=51491027",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=51491019",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=51491001",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=51491002",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=51501945",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=51501946",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=41301863",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=51501101",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=51501950",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=51501951", // 베이커리 24개
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=51501958",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=61791022",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=61791021",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=61701960",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=61701098",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=61602001",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=61602000",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=61601989",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=61601987",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=61701962",
        "https://mo.twosome.co.kr/mn/menuInfoDetail.do?menuCd=61701961", // 스낵 11개
    )

    val megacoffeeUrls = listOf(
        "https://www.mega-mgccoffee.com/menu/?menu_category1=1&menu_category2=1",
        "https://www.mega-mgccoffee.com/menu/?menu_category1=2&menu_category2=2"
    )

}