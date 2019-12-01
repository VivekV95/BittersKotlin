package com.vivekvishwanath.bitterskotlin.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.vivekvishwanath.bitterskotlin.util.CACHE_TYPE_OTHER
import kotlinx.android.parcel.Parcelize
import org.jetbrains.annotations.NotNull

@Entity(tableName = "cocktails")
@Parcelize
data class Cocktail(

    @ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean,

    @NotNull
    @ColumnInfo(name = "cache_type")
    var cacheType: Int? = CACHE_TYPE_OTHER,

    @ColumnInfo(name = "drink_id")
    @SerializedName("idDrink")
    @PrimaryKey
    val drinkId: String,

    @ColumnInfo(name = "drink_name")
    @SerializedName("strDrink")
    val drinkName: String,

    @ColumnInfo(name = "drink_tags")
    @SerializedName("strTags")
    val drinkTags: String?,

    @ColumnInfo(name = "drink_video")
    @SerializedName("strVideo")
    val drinkVideo: String?,

    @ColumnInfo(name = "drink_category")
    @SerializedName("strCategory")
    val drinkCategory: String?,

    @ColumnInfo(name = "drink_iba")
    @SerializedName("strIBA")
    val drinkIBA: String?,

    @ColumnInfo(name = "alcoholic")
    @SerializedName("strAlcoholic")
    val alcoholic: String?,

    @ColumnInfo(name = "drink_glass")
    @SerializedName("strGlass")
    val drinkGlass: String?,

    @ColumnInfo(name = "drink_instructions")
    @SerializedName("strInstructions")
    val drinkInstructions: String?,

    @ColumnInfo(name = "drink_image")
    @SerializedName("strDrinkThumb")
    val drinkImage: String?,

    @ColumnInfo(name = "str_ingredient_1")
    val strIngredient1: String?,

    @ColumnInfo(name = "str_ingredient_2")
    val strIngredient2: String?,

    @ColumnInfo(name = "str_ingredient_3")
    val strIngredient3: String?,

    @ColumnInfo(name = "str_ingredient_4")
    val strIngredient4: String?,

    @ColumnInfo(name = "str_ingredient_5")
    val strIngredient5: String?,

    @ColumnInfo(name = "str_ingredient_6")
    val strIngredient6: String?,

    @ColumnInfo(name = "str_ingredient_7")
    val strIngredient7: String?,

    @ColumnInfo(name = "str_ingredient_8")
    val strIngredient8: String?,

    @ColumnInfo(name = "str_ingredient_9")
    val strIngredient9: String?,

    @ColumnInfo(name = "str_ingredient_10")
    val strIngredient10: String?,

    @ColumnInfo(name = "str_ingredient_11")
    val strIngredient11: String?,

    @ColumnInfo(name = "str_ingredient_12")
    val strIngredient12: String?,

    @ColumnInfo(name = "str_ingredient_13")
    val strIngredient13: String?,

    @ColumnInfo(name = "str_ingredient_14")
    val strIngredient14: String?,

    @ColumnInfo(name = "str_ingredient_15")
    val strIngredient15: String?,

    @ColumnInfo(name = "str_measure_1")
    val strMeasure1: String?,

    @ColumnInfo(name = "str_measure_2")
    val strMeasure2: String?,

    @ColumnInfo(name = "str_measure_3")
    val strMeasure3: String?,

    @ColumnInfo(name = "str_measure_4")
    val strMeasure4: String?,

    @ColumnInfo(name = "str_measure_5")
    val strMeasure5: String?,

    @ColumnInfo(name = "str_measure_6")
    val strMeasure6: String?,

    @ColumnInfo(name = "str_measure_7")
    val strMeasure7: String?,

    @ColumnInfo(name = "str_measure_8")
    val strMeasure8: String?,

    @ColumnInfo(name = "str_measure_9")
    val strMeasure9: String?,

    @ColumnInfo(name = "str_measure_10")
    val strMeasure10: String?,

    @ColumnInfo(name = "str_measure_11")
    val strMeasure11: String?,

    @ColumnInfo(name = "str_measure_12")
    val strMeasure12: String?,

    @ColumnInfo(name = "str_measure_13")
    val strMeasure13: String?,

    @ColumnInfo(name = "str_measure_14")
    val strMeasure14: String?,

    @ColumnInfo(name = "str_measure_15")
    val strMeasure15: String?,

    @ColumnInfo(name = "date_modified")
    val dateModified: String?

): Parcelable

data class CocktailDbResponse(
    val drinks: List<Cocktail>
)