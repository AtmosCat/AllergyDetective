package com.example.allergydetective.data.model.market


import com.google.gson.annotations.SerializedName

data class MarketData(
    @SerializedName("display")
    val display: Int?,
    @SerializedName("items")
    val items: List<Item?>?,
    @SerializedName("lastBuildDate")
    val lastBuildDate: String?,
    @SerializedName("start")
    val start: Int?,
    @SerializedName("total")
    val total: Int?
)