package com.allergyguardian.allergyguardian.data.model.user

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
    val timestamp: String? = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
)

data class Comments(
    val id: String = UUID.randomUUID().toString(),
    val commenterEmail: String = "",
    val commenterPhoto: String = "",
    val commenterNickname: String = "",
    val detail: String = "",
    val like: Int = 0,
    var reply: List<Reply> = mutableListOf(),
    val report: Boolean = false,
    val timestamp: String? = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
)

data class Reply(
    val id: String = UUID.randomUUID().toString(),
    val replierEmail: String = "",
    val replierPhoto: String = "",
    val replierNickname: String = "",
    val detail: String = "",
    val like: Int = 0,
    val report: Boolean = false,
    val timestamp: String? = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
)
