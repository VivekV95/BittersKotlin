package com.vivekvishwanath.bitterskotlin.model

import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Cocktail(

    var isFavorite: Boolean,

    @SerializedName("idDrink")
    val drinkId: String,

    @SerializedName("strDrink")
    val drinkName: String,

    @SerializedName("strTags")
    val drinkTags: String?,

    @SerializedName("strVideo")
    val drinkVideo: String?,

    @SerializedName("strCategory")
    val drinkCategory: String?,

    @SerializedName("strIBA")
    val drinkIBA: String?,

    @SerializedName("strAlcoholic")
    val alcoholic: String?,

    @SerializedName("strGlass")
    val drinkGlass: String?,

    @SerializedName("strInstructions")
    val drinkInstructions: String?,

    @SerializedName("strDrinkThumb")
    val drinkImage: String?,

    val strIngredient1: String?,
    val strIngredient2: String?,
    val strIngredient3: String?,
    val strIngredient4: String?,
    val strIngredient5: String?,
    val strIngredient6: String?,
    val strIngredient7: String?,
    val strIngredient8: String?,
    val strIngredient9: String?,
    val strIngredient10: String?,
    val strIngredient11: String?,
    val strIngredient12: String?,
    val strIngredient13: String?,
    val strIngredient14: String?,
    val strIngredient15: String?,

    val strMeasure1: String?,
    val strMeasure2: String?,
    val strMeasure3: String?,
    val strMeasure4: String?,
    val strMeasure5: String?,
    val strMeasure6: String?,
    val strMeasure7: String?,
    val strMeasure8: String?,
    val strMeasure9: String?,
    val strMeasure10: String?,
    val strMeasure11: String?,
    val strMeasure12: String?,
    val strMeasure13: String?,
    val strMeasure14: String?,
    val strMeasure15: String?,

    val dateModified: String?
): Parcelable

data class CocktailDbResponse(
    val drinks: List<Cocktail>
)