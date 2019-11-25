package com.vivekvishwanath.bitterskotlin.util

data class Loading(val isLoading: Boolean)
data class Data<T>(val data: Event<T>?, val response: Event<ResponseMessage>?)
data class StateError(val responseMessage: ResponseMessage)

data class ResponseMessage(val message: String?, val responseType: ResponseType)

sealed class ResponseType {

    object Toast : ResponseType()

    object Dialog : ResponseType()

    object None : ResponseType()
}

class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled() =
        if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }

    fun peekContent() = content

    companion object {

        fun <T> dataEvent(data: T?): Event<T>? =
            data?.let {
                Event(data)
            }

        fun messageEvent(responseMessage: ResponseMessage?): Event<ResponseMessage>? {
            responseMessage?.let {
                return Event(responseMessage)
            }
            return null
        }
    }
}