package com.example.allergydetective.data.model.user

import java.util.UUID

data class Report(
    val id: String = UUID.randomUUID().toString(), // Report의 id 생성
    val type: String = "",  // Post, Comment, Reply
    val postId: String = "", // 게시대상 정보
    val postDetail: String = "",
    val reporterEmail: String = "", // 이메일계정
    val reporteeEmail: String = "", // 이메일계정
    val reportReason: String = "",
    val reportDetail: String = "",
    val timestamp: Any? = null // 제출시각
)