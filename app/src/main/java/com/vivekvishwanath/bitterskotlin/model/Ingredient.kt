package com.vivekvishwanath.bitterskotlin.model

import com.google.gson.annotations.SerializedName

data class Ingredient(

    @SerializedName("strIngredient1")
    val ingredientName: String?,

    val ingredientMeasurement: String?,

    val ingredientImage: String?
)

data class IngredientDbResponse(

    @SerializedName("drinks")
    val ingredients: List<Ingredient>
)