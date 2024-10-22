package com.allergyguardian.allergyguardian.data.model.franchise

import java.util.UUID

data class Menu(
    var id: String = "",
    var type: String = "",
    var brand: String = "",
    var subcat: String = "",
    var name: String = "",
    var allergy: String = "",
    var weight: String = "",
    var kcal: String = "",
    var natrium: String = "",
    var sugar: String = "",
    var fat: String = "",
    var protein: String = "",
    var origin: String = "",
    var nutrients: String = "",
    var imgurl: String = "",
    var url: String = "",
    var date: String = ""
)