package com.vivekvishwanath.bitterskotlin.ui.auth

import com.vivekvishwanath.bitterskotlin.ui.Event
import com.vivekvishwanath.bitterskotlin.ui.ResponseMessage
import com.vivekvishwanath.bitterskotlin.ui.ResponseType

sealed class AuthState<T>(
    val data: Event<T>? = null,
    val message: Event<ResponseMessage>? = null
) {
    class Authenticated<T>(data: T) : AuthState<T>(
        Event.dataEvent(
            data
        )
    )

    class Loading<T>(responseMessage: ResponseMessage? = null) :
        AuthState<T>(message = Event.messageEvent(responseMessage))

    class Error<T>(responseMessage: ResponseMessage, data: T? = null) :
        AuthState<T>(message = Event.messageEvent(responseMessage), data = Event.dataEvent(data))

    class NotAuthenticated<T>(responseMessage: ResponseMessage? = null):
        AuthState<T>(message = Event.messageEvent(responseMessage))
}