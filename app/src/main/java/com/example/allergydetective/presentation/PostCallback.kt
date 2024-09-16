package com.example.allergydetective.presentation

interface PostCallback {
    fun onSuccess()
    fun onFailure(exception: Throwable)
}

interface DeleteCommentCallback {
    fun onSuccess()
    fun onFailure(exception: Throwable)
}