package com.example.allergydetective.data.model.food


import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("body")
    val body: Body?,
    @SerializedName("header")
    val header: Header?
)