package com.vivekvishwanath.bitterskotlin.util

import com.vivekvishwanath.bitterskotlin.model.Cocktail
import com.vivekvishwanath.bitterskotlin.model.Ingredient

fun getMediumUrl(ingredientName: String): String =
    "https://www.thecocktaildb.com/images/ingredients/$ingredientName-Medium.png"

fun Cocktail.convertIngredientsToList(): List<Ingredient> {
    val ingredientList = arrayListOf<Ingredient>()
    if (!this.strIngredient1.isNullOrEmpty())
        ingredientList.add(
            Ingredient(
                this.strIngredient1.trim(),
                this.strMeasure1?.trim(),
                getMediumUrl(this.strIngredient1.trim())
            )
        )
    if (!this.strIngredient2.isNullOrEmpty())
        ingredientList.add(
            Ingredient(
                this.strIngredient2.trim(),
                this.strMeasure2?.trim(),
                getMediumUrl(this.strIngredient2.trim())
            )
        )
    if (!this.strIngredient3.isNullOrEmpty())
        ingredientList.add(
            Ingredient(
                this.strIngredient3.trim(),
                this.strMeasure3?.trim(),
                getMediumUrl(this.strIngredient3.trim())
            )
        )
    if (!this.strIngredient4.isNullOrEmpty())
        ingredientList.add(
            Ingredient(
                this.strIngredient4.trim(),
                this.strMeasure4?.trim(),
                getMediumUrl(this.strIngredient4.trim())
            )
        )
    if (!this.strIngredient5.isNullOrEmpty())
        ingredientList.add(
            Ingredient(
                this.strIngredient5.trim(),
                this.strMeasure5?.trim(),
                getMediumUrl(this.strIngredient5.trim())
            )
        )
    if (!this.strIngredient6.isNullOrEmpty())
        ingredientList.add(
            Ingredient(
                this.strIngredient6.trim(),
                this.strMeasure6?.trim(),
                getMediumUrl(this.strIngredient6.trim())
            )
        )
    if (!this.strIngredient7.isNullOrEmpty())
        ingredientList.add(
            Ingredient(
                this.strIngredient7.trim(),
                this.strMeasure7?.trim(),
                getMediumUrl(this.strIngredient7.trim())
            )
        )
    if (!this.strIngredient8.isNullOrEmpty())
        ingredientList.add(
            Ingredient(
                this.strIngredient8.trim(),
                this.strMeasure8?.trim(),
                getMediumUrl(this.strIngredient8.trim())
            )
        )
    if (!this.strIngredient9.isNullOrEmpty())
        ingredientList.add(
            Ingredient(
                this.strIngredient9.trim(),
                this.strMeasure9?.trim(),
                getMediumUrl(this.strIngredient9.trim())
            )
        )
    if (!this.strIngredient10.isNullOrEmpty())
        ingredientList.add(
            Ingredient(
                this.strIngredient10.trim(),
                this.strMeasure10?.trim(),
                getMediumUrl(this.strIngredient10.trim())
            )
        )
    if (!this.strIngredient11.isNullOrEmpty())
        ingredientList.add(
            Ingredient(
                this.strIngredient11.trim(),
                this.strMeasure11?.trim(),
                getMediumUrl(this.strIngredient11.trim())
            )
        )
    if (!this.strIngredient12.isNullOrEmpty())
        ingredientList.add(
            Ingredient(
                this.strIngredient12.trim(),
                this.strMeasure12?.trim(),
                getMediumUrl(this.strIngredient12.trim())
            )
        )
    if (!this.strIngredient13.isNullOrEmpty())
        ingredientList.add(
            Ingredient(
                this.strIngredient13.trim(),
                this.strMeasure13?.trim(),
                getMediumUrl(this.strIngredient13.trim())
            )
        )
    if (!this.strIngredient14.isNullOrEmpty())
        ingredientList.add(
            Ingredient(
                this.strIngredient14.trim(),
                this.strMeasure14?.trim(),
                getMediumUrl(this.strIngredient14.trim())
            )
        )
    if (!this.strIngredient15.isNullOrEmpty())
        ingredientList.add(
            Ingredient(
                this.strIngredient15.trim(),
                this.strMeasure15?.trim(),
                getMediumUrl(this.strIngredient15.trim())
            )
        )
    return ingredientList
}