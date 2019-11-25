package com.vivekvishwanath.bitterskotlin.network

import androidx.lifecycle.LiveData
import com.google.gson.JsonElement
import com.vivekvishwanath.bitterskotlin.model.CocktailDbResponse
import com.vivekvishwanath.bitterskotlin.model.IngredientDbResponse
import com.vivekvishwanath.bitterskotlin.util.GenericApiResponse
import io.reactivex.Flowable
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailDbService {

    @GET("search.php")
    fun getCocktailsByName(@Query("s") name: String): LiveData<GenericApiResponse<CocktailDbResponse>>

    @GET("filter.php")
    fun getCocktailsByIngredient(@Query("i") ingredient: String): LiveData<GenericApiResponse<CocktailDbResponse>>

    @GET("filter.php")
    fun getCocktailsByGlass(@Query("g") glass: String): LiveData<GenericApiResponse<CocktailDbResponse>>

    @GET("filter.php")
    fun getCocktailsByAlcoholic(@Query("a") isAlcoholic: String): LiveData<GenericApiResponse<CocktailDbResponse>>

    @GET("lookup.php")
    fun getCocktailById(@Query("i") id: String): LiveData<GenericApiResponse<CocktailDbResponse>>

    @GET("popular.php")
    fun getPopularCocktails(): LiveData<GenericApiResponse<CocktailDbResponse>>

    @GET("random.php")
    fun getRandomCocktail(): LiveData<GenericApiResponse<CocktailDbResponse>>

    @GET("list.php?i=list")
    fun getAllIngredients(): LiveData<GenericApiResponse<IngredientDbResponse>>
}