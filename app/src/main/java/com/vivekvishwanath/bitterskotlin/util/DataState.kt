package com.vivekvishwanath.bitterskotlin.util

data class DataState <T>(
    var error: Event<StateError>? = null,
    var loading: Loading = Loading(false),
    var data: Data<T>? = null
) {
    companion object {

        fun <T> error(responseMessage: ResponseMessage): DataState<T> =
            DataState(
                error = Event(StateError(responseMessage)),
                loading = Loading(false),
                data = null
            )

        fun <T> loading(isLoading: Boolean, cachedData: T? = null): DataState<T> =
            DataState(
                loading = Loading(isLoading)
            )

        fun <T> data(data: T? = null, responseMessage: ResponseMessage? = null): DataState<T> =
            DataState(
                error = null,
                loading = Loading(false),
                data = Data(
                    Event.dataEvent(data),
                    Event.messageEvent(responseMessage)
                )
            )
    }
}