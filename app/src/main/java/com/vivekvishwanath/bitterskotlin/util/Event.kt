package com.vivekvishwanath.bitterskotlin.util

class Event<T>(private val content: T) {

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

        fun messageEvent(message: String?): Event<String>? =
            message?.let {
                Event(message)
            }
    }
}