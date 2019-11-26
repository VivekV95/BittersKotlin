package com.vivekvishwanath.bitterskotlin.ui.main

import androidx.lifecycle.LiveData
import com.vivekvishwanath.bitterskotlin.di.scope.MainScope
import com.vivekvishwanath.bitterskotlin.model.Cocktail
import com.vivekvishwanath.bitterskotlin.repository.main.CocktailRepository
import com.vivekvishwanath.bitterskotlin.ui.BaseViewModel
import com.vivekvishwanath.bitterskotlin.ui.main.state.MainStateEvent
import com.vivekvishwanath.bitterskotlin.ui.main.state.MainViewState
import javax.inject.Inject

@MainScope
class CocktailViewModel @Inject constructor
    (private val repository: CocktailRepository) : BaseViewModel<MainStateEvent, MainViewState>() {

    override fun initNewViewState(): MainViewState {
        return MainViewState()
    }

    override fun handleStateEvent(stateEvent: MainStateEvent): LiveData<DataState<MainViewState>> =
        when (stateEvent) {
            is MainStateEvent.GetPopularCocktailsEvent -> {
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
