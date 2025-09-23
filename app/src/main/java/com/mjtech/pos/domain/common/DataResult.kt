package com.mjtech.pos.domain.common

sealed class DataResult<out T> {
    data class Success<out T>(val data: T) : DataResult<T>()
    data class Error(val error: String) : DataResult<Nothing>()
    object Loading : DataResult<Nothing>()
}