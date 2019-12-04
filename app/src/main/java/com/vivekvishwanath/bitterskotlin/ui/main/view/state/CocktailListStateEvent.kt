package com.vivekvishwanath.bitterskotlin.ui.main.view.state

sealed class CocktailListStateEvent {

    object GetPopularCocktailsEvent: CocktailListStateEvent()

    object GetFavoriteCocktailsEvent: CocktailListStateEvent()

    object None: CocktailListStateEvent()
}