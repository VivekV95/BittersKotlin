package com.vivekvishwanath.bitterskotlin.ui.main.view.state

import com.vivekvishwanath.bitterskotlin.model.Cocktail

data class CocktailListViewState(
    val popularCocktails: List<Cocktail>? = null,
    val favoriteCocktailIds: Set<Int>? = null
)