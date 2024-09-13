package com.example.allergydetective.data.model.user

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import java.util.UUID

data class Post(
    val id: String = UUID.randomUUID().toString(),
    val category: String = "",
    val posterEmail: String = "",
    val posterPhoto: String = "",
    val posterNickname: String = "",
    val title: String = "",
    val detail: String = "",
    val detailPhoto: MutableList<String> = mutableListOf(),
    val comments: List<Comments> = mutableListOf(),
    var scrap: Int = 0,
    val report: Boolean = false,
    val timestamp: Any? = FieldValue.serverTimestamp())

data class Comments(
    val id: String = UUID.randomUUID().toString(),
    val commenterEmail: String = "",
    val commenterPhoto: String = "",
    val commenterNickname: String = "",
    val detail: String = "",
    val like: Int = 0,
    val reply: List<Reply> = mutableListOf(),
    val report: Boolean = false,
    val timestamp: Any? = FieldValue.serverTimestamp())

data class Reply(
    val id: String = UUID.randomUUID().toString(),
    val replierEmail: String = "",
    val replierPhoto: String = "",
    val replierNickname: String = "",
    val detail: String = "",
    val like: Int = 0,
    val report: Boolean = false,
    val timestamp: Any? = FieldValue.serverTimestamp())
