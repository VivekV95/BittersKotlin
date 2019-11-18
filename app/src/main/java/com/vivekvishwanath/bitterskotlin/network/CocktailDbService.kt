package com.vivekvishwanath.bitterskotlin.network

import com.google.gson.JsonElement
import com.vivekvishwanath.bitterskotlin.model.CocktailDbResponse
import com.vivekvishwanath.bitterskotlin.model.IngredientDbResponse
import io.reactivex.Flowable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailDbService {

    @GET("search.php")
    fun getCocktailsByName(@Query("s") name: String): Flowable<CocktailDbResponse>

    @GET("filter.php")
    fun getCocktailsByIngredient(@Query("i") ingredient: String): Flowable<CocktailDbResponse>

    @GET("filter.php")
    fun getCocktailsByGlass(@Query("g") glass: String): Flowable<CocktailDbResponse>

    @GET("filter.php")
    fun getCocktailsByAlcoholic(@Query("a") isAlcoholic: String): Flowable<CocktailDbResponse>

    @GET("lookup.php")
    fun getCocktailById(@Query("i") id: String): Flowable<CocktailDbResponse>

    @GET("popular.php")
    fun getPopularCocktails(): Flowable<CocktailDbResponse>

    @GET("random.php")
    fun getRandomCocktail(): Flowable<CocktailDbResponse>

    @GET("list.php?i=list")
    fun getAllIngredients(): Flowable<IngredientDbResponse>
}