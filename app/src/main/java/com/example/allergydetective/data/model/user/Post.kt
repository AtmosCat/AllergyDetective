package com.example.allergydetective.data.model.user

data class Post(
    val id: String = "",
    val category: String = "",
    val posterPhoto: String = "",
    val posterName : String = "",
    val title: String = "",
    val detail: String = "",
    val detailPhoto: List<String> =mutableListOf(),
    val comments: List<Comments> = mutableListOf(),
    val scrap: Int = 0,
    val report: Boolean = false)

data class Comments(
    val id: String = "",
    val commenterPhoto: String = "",
    val commenterName: String = "",
    val detail: String = "",
    val like: Int = 0,
    val reply: List<Reply> = mutableListOf(),
    val report: Boolean = false)

data class Reply(
    val id: String = "",
    val replierPhoto: String = "",
    val replierName: String = "",
    val detail: String = "",
    val like: Int = 0,
    val report: Boolean = false)