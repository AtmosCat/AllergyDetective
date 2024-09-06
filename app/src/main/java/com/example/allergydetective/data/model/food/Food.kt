package com.example.allergydetective.data.model.food


// 균일화된 레시피
// 나중에 추가되는 다른 식품 데이터들을 균일화해서 하나의 Food로 관리하기
// 처음에 쓰는 data.go.kr은 gonggong으로 관리
data class Food(
    val allergy: String? = "", // 알러지유발성분
    val imgurl1: String? = "", // 이미지 1
    val imgurl2: String? = "", // 이미지 2
    val manufacture: String? = "", // 제조사
    val nutrient: String? = "", // 영양성분
    val prdkind: String? = "", // 식품구분
    val prdlstNm: String? = "", // 이름
    val rawmtrl: String? = "", // 원재료
    val seller: String = "", // 판매원
    val prdlstReportNo: String = "", // 품목보고번호
    val like: Int = 0 // 관심목록 추가
)