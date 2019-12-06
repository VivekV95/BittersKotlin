package com.vivekvishwanath.bitterskotlin.network

import android.app.Application
import com.vivekvishwanath.bitterskotlin.R
import com.vivekvishwanath.bitterskotlin.util.LiveDataCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import com.google.gson.Gson

class CocktailDbServiceWrapper(application: Application) {

    private val BASE_URL =
        "https://www.thecocktaildb.com/api/json/v2/${application.resources.getString(R.string.cocktailDbApiKey)}/"

    val interceptor  = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

    var gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .build()

    val cocktailDbService = retrofit.create(CocktailDbService::class.java)
}