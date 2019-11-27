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
        val update = getCurrentViewStateOrNew().copy(popularCocktails = cocktails)
        _viewState.value = update
    }

    fun logOut() {
        repository.logOut()
    }

    fun cancelActiveJobs() {
        repository.cancelActiveJobs()
    }
}
