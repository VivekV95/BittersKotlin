package com.vivekvishwanath.bitterskotlin.ui.main.view.state

import com.vivekvishwanath.bitterskotlin.model.Cocktail

data class CocktailListViewState(
    var cocktailFields: CocktailFields = CocktailFields(),
    var viewCocktailField: ViewCocktailFields = ViewCocktailFields(),
    var favoriteIds: Set<Int> = setOf()
) {

    data class CocktailFields(
        var popularCocktails: List<Cocktail> = arrayListOf(),
        var customCocktails: List<Cocktail> = arrayListOf(),
        var favoriteCocktails: List<Cocktail> = arrayListOf()
    )

    data class ViewCocktailFields(
        var currentCocktail: Cocktail? = null
    )
}
