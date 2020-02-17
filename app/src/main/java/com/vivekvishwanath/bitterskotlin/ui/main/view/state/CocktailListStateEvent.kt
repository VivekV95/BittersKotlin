package com.vivekvishwanath.bitterskotlin.ui.main.view.state

import com.vivekvishwanath.bitterskotlin.model.Cocktail

sealed class CocktailListStateEvent {

    object GetPopularCocktailsEvent: CocktailListStateEvent()

    object GetFavoriteCocktailsEvent: CocktailListStateEvent()

    data class AddFavoriteCocktailEvent(
        val cocktail: Cocktail
    ): CocktailListStateEvent()

    data class DeleteFavoriteCocktailEvent(
        val cocktail: Cocktail
    ): CocktailListStateEvent()

    object None: CocktailListStateEvent()
}