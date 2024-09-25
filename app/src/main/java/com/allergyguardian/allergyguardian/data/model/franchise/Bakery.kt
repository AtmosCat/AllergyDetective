package com.allergyguardian.allergyguardian.data.model.franchise

data class Bakery(
    val brand: String = "",
    val type: String = "",
    val name: String = "",
    val allergy: List<String> = emptyList(),
    val weight: String = "",
    val kcal: String = "",
    val natrium: String = "",
    val sugar: String = "",
    val fat: String = "",
    val protein: String = "",
    val nutrients: String = "",
    val img: String = ""
)
