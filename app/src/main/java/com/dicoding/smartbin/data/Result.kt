package com.dicoding.smartbin.data

import com.dicoding.smartbin.utils.EventHandlerToast

sealed class Result<out R> private constructor() {
    data class Success<out T>(val data: T): Result<T>()
    data class Error(val error: EventHandlerToast<String>): Result<Nothing>()
    object Loading: Result<Nothing>()
}