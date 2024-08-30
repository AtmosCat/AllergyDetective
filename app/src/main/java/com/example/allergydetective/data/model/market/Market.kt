package com.example.allergydetective.data.model.market

object MarketManager {
    var marketResults = emptyList<Market>()
}

// 균일화된 레시피
// 나중에 추가되는 다른 식품 데이터들을 균일화해서 하나의 Food로 관리하기
// 처음에 쓰는 data.go.kr은 gonggong으로 관리
data class Market(
    val brand: String?, // 브랜드
    val category1: String?, // 대분류
    val category2: String?, // 중분류
//    val category3: String?, // 소분류
//    val category4: String?, // 세분류
//    val hprice: Int?, // 최고가
    val image: String?, // 이미지
    val link: String?, // 상품정보 URL
    val lprice: String?, // 최저가
    val maker: String?, // 제조사
    val mallName: String?, // 상품을 판매하는 쇼핑몰
//    val productid: Long?, // 네이버쇼핑 상품 ID
//    val producttype: Int?, // 상품군 타입(일반, 중고, 단종, 판매예정)
    val title: String? // 상품 이름
)