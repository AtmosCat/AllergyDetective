package com.example.allergydetective.data.model.food


object FoodManager {
    var foods = listOf<Food>()

    val allergyList: List<String> = listOf("알류","계란","우유","메밀","땅콩","대두","밀","고등어","게","새우",
        "돼지고기","복숭아","토마토","아황산류","이산화황","호두","닭고기","쇠고기","소고기","오징어","잣",
        "조개류","조개","굴","전복","홍합","조개류(굴)","조개류(전복)","조개류(홍합)")

    var selectedCategory = ""
    var selectedAllergies = listOf("우유")

    var filteredFoods = listOf<Food>()

    var arrangedFoods = listOf<Food>()
}

// 균일화된 레시피
// 나중에 추가되는 다른 식품 데이터들을 균일화해서 하나의 Food로 관리하기
// 처음에 쓰는 data.go.kr은 gonggong으로 관리
data class Food(
    val allergy: String?, // 알러지유발성분
    val imgurl1: String?, // 이미지 1
    val imgurl2: String?, // 이미지 2
    val manufacture: String?, // 제조사
    val nutrient: String?, // 영양성분
    val prdkind: String?, // 식품구분
    val prdlstNm: String?, // 이름
    val rawmtrl: String?, // 원재료
    val seller: String, // 판매원
    val prdlstReportNo: String, // 품목보고번호
    val like: Int // 관심목록 추가
)