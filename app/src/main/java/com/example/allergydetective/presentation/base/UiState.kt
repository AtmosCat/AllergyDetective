package com.example.allergydetective.presentation.base

sealed class UiState<out T> {
    // 로딩 상태 -> 데이터를 가질 필요가 없으므로 object로 선언
    object Loading : UiState<Nothing>()

    // 성공했을 때에는 받아온 데이터를 data에 저장
    data class Success<out T>(val data: T) : UiState<T>()

    // 에러 상태 -> string 타입의 메시지
    data class Error(val message: String) : UiState<Nothing>()
}