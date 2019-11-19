package com.vivekvishwanath.bitterskotlin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.vivekvishwanath.bitterskotlin.util.*

abstract class NetworkBoundResource<ResponseObject, ViewStateType> {

    protected val result = MediatorLiveData<DataState<ViewStateType>>()

    init {

        result.value = DataState.loading(true)

        val apiRespose = createCall()

        result.addSource(apiRespose) { response ->
            result.removeSource(apiRespose)

            handleNetworkCall(response)
        }
    }

    private fun handleNetworkCall(response: GenericApiResponse<ResponseObject>?) {
        when (response) {
            is ApiSuccessResponse -> {
                handleApiSuccessResponse(response)
            }
            is ApiErrorResponse -> {
                onReturnError(response.errorMessage)
            }
            is ApiEmptyResponse -> {
                onReturnError("Returned Nothing")
            }
        }
    }

    fun onReturnError(message: String) {
        result.value = DataState.error(message)
    }

    abstract fun handleApiSuccessResponse(response: ApiSuccessResponse<ResponseObject>)

    abstract fun createCall(): LiveData<GenericApiResponse<ResponseObject>>

    fun asLiveData() = result as LiveData<DataState<ViewStateType>>
}