package com.vivekvishwanath.bitterskotlin.util

sealed class AuthState<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Authenticated<T>(data: T) : AuthState<T>(data)

    class Loading<T>(data: T? = null) : AuthState<T>(data)

    class Error<T>(message: String, data: T? = null) : AuthState<T>(data, message)

    class NotAuthenticated<T>(data: T? = null): AuthState<T>(data)
}