package com.vivekvishwanath.bitterskotlin.util

import androidx.lifecycle.LiveData

class AbsentLiveData<T: Any?> private constructor(): LiveData<T>() {

    init {
        postValue(null)
    }

    companion object {
        fun <T> create(): LiveData<T> = AbsentLiveData()
    }
}