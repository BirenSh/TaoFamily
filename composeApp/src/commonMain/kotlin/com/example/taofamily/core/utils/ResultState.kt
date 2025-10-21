package com.example.taofamily.core.utils
sealed class UiState<out T>{
    data object Ideal : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data class Error(val errorMessage: String) : UiState<Nothing>()
    data class Success<T>(val result: T): UiState<T>()
}