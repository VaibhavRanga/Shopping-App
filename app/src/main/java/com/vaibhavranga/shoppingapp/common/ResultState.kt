package com.vaibhavranga.shoppingapp.common

sealed class ResultState<out T> {
    data object Loading : ResultState<Nothing>()
    data class Error(val error: String) : ResultState<Nothing>()
    data class Success<out T>(val data: T) : ResultState<T>()
}