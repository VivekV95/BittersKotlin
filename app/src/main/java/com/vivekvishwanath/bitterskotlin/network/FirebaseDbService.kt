package com.vivekvishwanath.bitterskotlin.network

import androidx.lifecycle.LiveData
import com.vivekvishwanath.bitterskotlin.model.Cocktail
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface FirebaseDbService {

    @GET("users/{uid}")
    fun getFavoriteCocktails(
        @Query("auth")authToken: String,
        @Path("uid")uid: String): LiveData<List<Cocktail>>
}