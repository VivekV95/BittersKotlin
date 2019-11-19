package com.vivekvishwanath.bitterskotlin.ui.main.state

import com.vivekvishwanath.bitterskotlin.model.Cocktail

data class MainViewState(
    val popularCocktails: List<Cocktail>? = null
)