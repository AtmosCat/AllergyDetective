package com.allergyguardian.allergyguardian.data.model.food


import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("body")
    val body: Body?,
    @SerializedName("header")
    val header: Header?
)