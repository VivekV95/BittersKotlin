package com.vivekvishwanath.bitterskotlin.util

sealed class AuthState<T>(
    val data: Event<T>? = null,
    val message: Event<ResponseMessage>? = null
) {
    class Authenticated<T>(data: T) : AuthState<T>(Event.dataEvent(data))

    class Loading<T> : AuthState<T>()

    class Error<T>(message: String, data: T? = null) :
        AuthState<T>(message = Event.messageEvent(ResponseMessage(message, ResponseType.Dialog)))

    class NotAuthenticated<T>: AuthState<T>()
}