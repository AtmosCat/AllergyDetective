package com.example.allergydetective.data.model.user

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import java.util.UUID

data class Post(
    val id: String = UUID.randomUUID().toString(),
    val category: String = "",
    val posterPhoto: String = "",
    val posterName: String = "",
    val title: String = "",
    val detail: String = "",
    val detailPhoto: List<String> =mutableListOf(),
    val comments: List<Comments> = mutableListOf(),
    var scrap: Int = 0,
    val report: Boolean = false,
    val timestamp: Any? = null)

data class Comments(
    val id: String = UUID.randomUUID().toString(),
    val commenterPhoto: String = "",
    val commenterName: String = "",
    val detail: String = "",
    val like: Int = 0,
    val reply: List<Reply> = mutableListOf(),
    val report: Boolean = false,
    val timestamp: Any? = null)

data class Reply(
    val id: String = UUID.randomUUID().toString(),
    val replierPhoto: String = "",
    val replierName: String = "",
    val detail: String = "",
    val like: Int = 0,
    val report: Boolean = false,
    val timestamp: Any? = null)
