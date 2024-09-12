package com.example.allergydetective.data.model.user

data class Report(
    val type: String = "",  // Post, Comment, Reply
    val id: String = "", // 게시대상 정보
    val reporter: String = "", // 이메일계정
    val reportee: String = "", // 이메일계정
    val reportReason: String = "",
    val reportDetail: String = "",
    val timestamp: Any? = null // 제출시각
)