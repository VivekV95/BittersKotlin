package com.vivekvishwanath.bitterskotlin.util

import androidx.lifecycle.LiveData
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Taken from the GithubBrowserSample app from Google's
 * architecture-components-samples at
 * https://github.com/android/architecture-components-samples/tree/master/GithubBrowserSample
 */
class LiveDataCallAdapter<R>(private val responseType: Type) :
    CallAdapter<R, LiveData<GenericApiResponse<R>>> {

    override fun responseType() = responseType

    override fun adapt(call: Call<R>): LiveData<GenericApiResponse<R>> {
        return object : LiveData<GenericApiResponse<R>>() {
            private var started = AtomicBoolean(false)
            private var completed = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false, true)) {
                    call.enqueue(object : Callback<R> {
                        override fun onResponse(call: Call<R>, response: Response<R>) {
                            completed.set(true)
                            postValue(GenericApiResponse.create(response))
                        }

                        override fun onFailure(call: Call<R>, throwable: Throwable) {
                            postValue(GenericApiResponse.create(throwable))
                        }
                    })
                }
            }

            override fun onInactive() {
                super.onInactive()
                if (completed.compareAndSet(false, true)) {
                    call.cancel()
                }
            }
        }
    }
}