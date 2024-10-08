package com.allergyguardian.allergyguardian.data.model.franchise

import java.util.UUID

data class Menu(
    val id : String = UUID.randomUUID().toString(),
    var type: String = "", // 카페, 패스트푸드, 베이커리/도넛...
    var brand: String = "", // 브랜드
    var name: String = "", // 메뉴명
    var allergy: String = "", // 알러지유발성분
    var weight: String = "", // 중량
    var kcal: String = "", // 열량
    var natrium: String = "", // 나트륨
    var sugar: String = "", // 당류
    var fat: String = "", // 지방
    var protein: String = "", // 단백질
    var caffeine: String = "", // 카페인
    var nutrients: String = "", // 영양성분
    var imgurl: String = "", // 이미지
    var hotice: String = ""
)