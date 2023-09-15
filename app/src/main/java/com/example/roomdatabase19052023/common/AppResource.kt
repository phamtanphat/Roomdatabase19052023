package com.example.roomdatabase19052023.common

sealed class AppResource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : AppResource<T>(data)
    class Error<T>(message: String) : AppResource<T>(message = message)
    class Loading<T> : AppResource<T>()
}