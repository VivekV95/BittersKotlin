package com.vivekvishwanath.bitterskotlin.network

import android.app.Application
import com.vivekvishwanath.bitterskotlin.R
import com.vivekvishwanath.bitterskotlin.util.LiveDataCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FirebaseDbServiceWrapper {
    private val BASE_URL =
        "https://bitters-4943d.firebaseio.com/"

    private val retrofit = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .build()

    val firebaseService = retrofit.create(FirebaseDbService::class.java)
}