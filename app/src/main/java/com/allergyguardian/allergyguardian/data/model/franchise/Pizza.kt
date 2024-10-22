package com.allergyguardian.allergyguardian.data.model.franchise

data class Pizza(
    val id: String = "",
    val type: String = "",
    val brand: String = "",
    val subcat: String = "",
    val name: String = "",
    val allergy: List<String> = emptyList(),
    val weight: String = "",
    val kcal: String = "",
    val natrium: String = "",
    val sugar: String = "",
    val fat: String = "",
    val protein: String = "",
    val origin: String = "",
    val nutrients: String = "",
    val imgurl: String = "",
    val url: String = "",
    val date: String = ""
)