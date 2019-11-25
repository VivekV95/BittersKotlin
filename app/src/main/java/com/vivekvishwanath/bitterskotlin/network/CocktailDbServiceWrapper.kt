package com.vivekvishwanath.bitterskotlin.network

import android.app.Application
import com.vivekvishwanath.bitterskotlin.R
import com.vivekvishwanath.bitterskotlin.util.LiveDataCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CocktailDbServiceWrapper(application: Application) {

    private val BASE_URL =
        "https://www.thecocktaildb.com/api/json/v2/${application.resources.getString(R.string.cocktailDbApiKey)}/"

    private val retrofit = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .build()

    val cocktailDbService = retrofit.create(CocktailDbService::class.java)


}