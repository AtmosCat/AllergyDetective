package com.allergyguardian.allergyguardian.data.model.market


import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("brand")
    val brand: String?,
    @SerializedName("category1")
    val category1: String?,
    @SerializedName("category2")
    val category2: String?,
    @SerializedName("category3")
    val category3: String?,
    @SerializedName("category4")
    val category4: String?,
    @SerializedName("hprice")
    val hprice: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("link")
    val link: String?,
    @SerializedName("lprice")
    val lprice: String?,
    @SerializedName("maker")
    val maker: String?,
    @SerializedName("mallName")
    val mallName: String?,
    @SerializedName("productId")
    val productId: String?,
    @SerializedName("productType")
    val productType: String?,
    @SerializedName("title")
    val title: String?
)