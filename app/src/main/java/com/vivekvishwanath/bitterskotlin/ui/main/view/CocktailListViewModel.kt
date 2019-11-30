package com.vivekvishwanath.bitterskotlin.ui.main.view

import androidx.lifecycle.LiveData
import com.vivekvishwanath.bitterskotlin.di.scope.MainScope
import com.vivekvishwanath.bitterskotlin.model.Cocktail
import com.vivekvishwanath.bitterskotlin.repository.main.CocktailRepository
import com.vivekvishwanath.bitterskotlin.ui.BaseViewModel
import com.vivekvishwanath.bitterskotlin.ui.main.DataState
import com.vivekvishwanath.bitterskotlin.ui.main.view.state.CocktailListStateEvent
import com.vivekvishwanath.bitterskotlin.ui.main.view.state.CocktailListViewState
import javax.inject.Inject

@MainScope
class CocktailListViewModel @Inject constructor
    (private val repository: CocktailRepository) : BaseViewModel<CocktailListStateEvent, CocktailListViewState>() {

    override fun initNewViewState(): CocktailListViewState {
        return CocktailListViewState()
    }

    override fun handleStateEvent(stateEvent: CocktailListStateEvent): LiveData<DataState<CocktailListViewState>> =
        when (stateEvent) {
            is CocktailListStateEvent.GetPopularCocktailsEvent -> {
                repository.getPopularCocktails()
            }
        }

    fun setPopularCocktailsData(cocktails: List<Cocktail>) {
        val update = getCurrentViewStateOrNew().copy()
        update.cocktailFields.popularCocktails = cocktails
        _viewState.value = update
    }

    fun setFavoriteIdsData(ids: Set<Int>) {
        val update = getCurrentViewStateOrNew().copy()
        update.favoriteIds = ids
        _viewState.value = update
    }

    fun getFavoriteIds() = repository.getFavoriteIds()

    fun setCurrentCocktail(cocktail: Cocktail) {
        _viewState.value?.viewCocktailField?.currentCocktail = cocktail
    }

    suspend fun addFavoriteCocktail(cocktail: Cocktail) = repository.addToFavorites(cocktail)

    suspend fun deleteFavoriteCocktail(cocktail: Cocktail) = repository.deleteFromFavorites(cocktail)

    fun refreshFavorites() {
        repository.refreshFavorites()
    }

    fun logOut() {
        repository.logOut()
    }

    fun cancelActiveJobs() {
        repository.cancelJobs()
    }
}
