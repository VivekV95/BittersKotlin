package com.vivekvishwanath.bitterskotlin.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.vivekvishwanath.bitterskotlin.ui.ResponseMessage
import com.vivekvishwanath.bitterskotlin.ui.ResponseType
import com.vivekvishwanath.bitterskotlin.ui.main.DataState
import com.vivekvishwanath.bitterskotlin.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

/**
 * Concept borrowed from the GithubBrowserSample app from Google's
 * architecture-components-samples at
 * https://github.com/android/architecture-components-samples/tree/master/GithubBrowserSample
 */
abstract class NetworkBoundResource<ResponseObject, CacheObject, ViewStateType>(
    isNetworkAvailable: Boolean,
    isNetworkRequest: Boolean,
    shouldCancelIfNoInternet: Boolean,
    shouldLoadFromCache: Boolean
) {

    protected val result = MediatorLiveData<DataState<ViewStateType>>()

    protected lateinit var job: CompletableJob
    protected lateinit var coroutineScope: CoroutineScope

    init {
        setJob(initNewJob())
        setValue(DataState.loading(true, cachedData = null))

        if (shouldLoadFromCache) {
            //TODO: Load data from cache
        }

        if (isNetworkRequest) {
            when {
                isNetworkAvailable -> doNetworkRequest()
                shouldCancelIfNoInternet -> onReturnError(
                    UNABLE_TODO_OPERATION_WO_INTERNET
                )
                else -> doCacheRequest()
            }
        } else
            doCacheRequest()
    }

    private fun doCacheRequest() {
        coroutineScope.launch {
            //delay

            createCacheRequestAndReturn()
        }
    }

    private fun doNetworkRequest() {
        coroutineScope.launch {
            //delay(TESTING_NETWORK_DELAY)

            GlobalScope.launch (Main) {
                Log.d(LOG_TAG, "${this.javaClass.simpleName}: ${Thread.currentThread()}")
                val apiResponse = createCall()
                apiResponse?.let {
                    result.addSource(apiResponse) { response ->
                        result.removeSource(apiResponse)

                        coroutineScope.launch {
                            Log.d(LOG_TAG, "${this.javaClass.simpleName}: ${Thread.currentThread()}")
                            handleNetworkCall(response)
                        }
                    }
                }
            }
        }

        GlobalScope.launch(IO) {
            delay(NETWORK_TIMEOUT)

            if (!job.isCompleted) {
                Log.d(LOG_TAG, "${this.javaClass.simpleName}: Job network timeout")
                job.cancel(CancellationException(NETWORK_TIMEOUT_MESSAGE))
            }
        }
    }

    private suspend fun handleNetworkCall(response: GenericApiResponse<ResponseObject>?) {
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

    fun setValue(dataState: DataState<ViewStateType>) {
        result.value = dataState
    }

    fun onCompleteJob(dataState: DataState<ViewStateType>) {
        GlobalScope.launch(Main) {
            job.complete()
            setValue(dataState)
        }
    }

    fun onReturnError(message: String?) {
        var msg = message
        if (msg == null)
            msg = ERROR_UNKNOWN
        else if (isNetworkError(msg))
            msg = ERROR_CHECK_NETWORK_CONNECTION
        onCompleteJob(DataState.error(
            ResponseMessage(
                msg,
                ResponseType.Dialog
            )
        ))
    }

    @UseExperimental(InternalCoroutinesApi::class)
    private fun initNewJob(): Job {
        Log.d(LOG_TAG, "initNewJob: called.")
        job = Job()
        job.invokeOnCompletion(true, true, object : CompletionHandler {
            override fun invoke(cause: Throwable?) {
                if (job.isCancelled) {
                    Log.d(LOG_TAG, "NetworkBoundResource: Job has been cancelled.")
                    cause?.let { throwable ->
                        throwable.message?.let {
                            onReturnError(it)
                        }
                    } ?: onReturnError("Unknown error")
                } else {
                    Log.d(LOG_TAG, "${this.javaClass.simpleName}: Job has been completed")
                }
            }
        })
        coroutineScope = CoroutineScope(IO + job)
        return job
    }

    abstract suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<ResponseObject>)

    abstract fun createCall(): LiveData<GenericApiResponse<ResponseObject>>?

    fun asLiveData() = result as LiveData<DataState<ViewStateType>>

    abstract suspend fun createCacheRequestAndReturn()

    abstract fun loadFromCache(): LiveData<ViewStateType>

    abstract suspend fun updateLocalDb(cacheObject: CacheObject?)

    abstract fun setJob(job: Job)
}