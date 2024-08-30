package com.example.allergydetective.data.model.food


import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("item")
    val item: ItemX?
)