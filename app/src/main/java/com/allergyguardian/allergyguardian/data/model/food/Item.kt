package com.allergyguardian.allergyguardian.data.model.food


import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("item")
    val item: ItemX?
)