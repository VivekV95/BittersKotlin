package com.vivekvishwanath.bitterskotlin.ui.main.view.viewmodel

import androidx.lifecycle.LiveData
import com.vivekvishwanath.bitterskotlin.R
import com.vivekvishwanath.bitterskotlin.di.scope.MainScope
import com.vivekvishwanath.bitterskotlin.model.Cocktail
import com.vivekvishwanath.bitterskotlin.repository.main.CocktailRepository
import com.vivekvishwanath.bitterskotlin.ui.BaseViewModel
import com.vivekvishwanath.bitterskotlin.ui.main.DataState
import com.vivekvishwanath.bitterskotlin.ui.main.view.state.CocktailListStateEvent
import com.vivekvishwanath.bitterskotlin.ui.main.view.state.CocktailListStateEvent.*
import com.vivekvishwanath.bitterskotlin.ui.main.view.state.CocktailListViewState
import com.vivekvishwanath.bitterskotlin.util.AbsentLiveData
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Named

@MainScope
class CocktailListViewModel @Inject constructor
    (
    private val repository: CocktailRepository,
    @Named("popularCall") var shouldCall: AtomicBoolean
) : BaseViewModel<CocktailListStateEvent, CocktailListViewState>() {

    init {
        if (!shouldCall.get()) {
            setStateEvent(GetPopularCocktailsEvent)
            setStateEvent(GetFavoriteCocktailsEvent)
            shouldCall.set(true)
        }
    }

    override fun initNewViewState(): CocktailListViewState {
        return CocktailListViewState()
    }

    override fun handleStateEvent(stateEvent: CocktailListStateEvent): LiveData<DataState<CocktailListViewState>> =
        when (stateEvent) {
            is GetPopularCocktailsEvent -> {
                repository.getPopularCocktails()
            }
            is GetFavoriteCocktailsEvent -> {
                repository.getFavoriteCocktails()
            }
            is AddFavoriteCocktailEvent -> {
                AbsentLiveData.create()
            }
            is DeleteFavoriteCocktailEvent -> {
                AbsentLiveData.create()
            }
            is None -> {
                AbsentLiveData.create()
        }
        }

    fun setPopularCocktailsData(cocktails: List<Cocktail>) {
        val update = getCurrentViewStateOrNew().copy()
        update.cocktailFields.popularCocktails = cocktails
        _viewState.value = update
    }

    fun setFavoriteCocktailsData(cocktails: List<Cocktail>) {
        val update = getCurrentViewStateOrNew().copy()
        update.cocktailFields.favoriteCocktails = cocktails
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

    fun addFavoriteCocktail(cocktail: Cocktail) {
        GlobalScope.launch(IO) {
            val result = repository.addToFavorites(cocktail)
            result.data?.data?.getContentIfNotHandled().let {
                if (it == "Success")
                    GlobalScope.launch(Main) {
                        setStateEvent(GetFavoriteCocktailsEvent)
                    }
            }
        }
    }

    fun deleteFavoriteCocktail(cocktail: Cocktail) {
        GlobalScope.launch(IO) {
            val result = repository.deleteFromFavorites(cocktail)
            result.data?.data?.getContentIfNotHandled().let {
                if (it == "Success")
                    GlobalScope.launch(Main) {
                        setStateEvent(GetFavoriteCocktailsEvent)
                    }
            }
        }
    }

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
