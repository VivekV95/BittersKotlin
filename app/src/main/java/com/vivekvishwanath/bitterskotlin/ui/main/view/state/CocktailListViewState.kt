package com.vivekvishwanath.bitterskotlin.ui.main.view.state

import com.vivekvishwanath.bitterskotlin.model.Cocktail
import com.vivekvishwanath.bitterskotlin.util.TYPE_POPULAR

data class CocktailListViewState(
    var cocktailFields: CocktailFields = CocktailFields(),
    var viewCocktailField: ViewCocktailFields = ViewCocktailFields(),
    var favoriteIds: Set<Int> = setOf()
) {

    data class CocktailFields(
        var popularCocktails: List<Cocktail> = arrayListOf(),
        var customCocktails: List<Cocktail> = arrayListOf(),
        var favoriteCocktails: List<Cocktail> = arrayListOf(),
        var searchQuery: String = "",
        var currentCocktailType: String = TYPE_POPULAR,
        val cocktailListPositions: HashMap<Int, Int> =
            hashMapOf(0 to 0, 1 to 0, 2 to 0)
    )

    data class ViewCocktailFields(
        var currentCocktail: Cocktail? = null
    )
}
