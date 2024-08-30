package com.example.allergydetective.data.model.food


import com.google.gson.annotations.SerializedName

data class Header(
    @SerializedName("resultCode")
    val resultCode: String?,
    @SerializedName("resultMessage")
    val resultMessage: String?
)