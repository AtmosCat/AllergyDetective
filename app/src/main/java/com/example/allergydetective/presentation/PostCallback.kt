package com.example.allergydetective.presentation

interface PostCallback {
    fun onSuccess()
    fun onFailure(exception: Throwable)
}