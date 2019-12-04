package com.vivekvishwanath.bitterskotlin.network

import androidx.lifecycle.LiveData
import com.vivekvishwanath.bitterskotlin.model.Cocktail
import com.vivekvishwanath.bitterskotlin.util.GenericApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface FirebaseDbService {

    @GET("users/{uid}")
    fun getFavoriteCocktails(
        @Path("uid")uid: String,
        @Query("auth")authToken: String): LiveData<GenericApiResponse<List<Cocktail>>>
}