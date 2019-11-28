package com.vivekvishwanath.bitterskotlin.ui.main.view.state

sealed class CocktailListStateEvent {

    object GetPopularCocktailsEvent: CocktailListStateEvent()
}