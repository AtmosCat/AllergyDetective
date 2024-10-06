package com.allergyguardian.allergyguardian.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.allergyguardian.allergyguardian.presentation.base.UiState
import com.google.firebase.firestore.FirebaseFirestore

class FranchiseViewModel(application: Application) : AndroidViewModel(application) {

    private val db = FirebaseFirestore.getInstance()

    private val _uiState: MutableLiveData<UiState<Any>> = MutableLiveData()
    val uiState: LiveData<UiState<Any>> = _uiState































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


}