package com.vivekvishwanath.bitterskotlin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import com.vivekvishwanath.bitterskotlin.di.scope.MainScope
import com.vivekvishwanath.bitterskotlin.model.Cocktail
import com.vivekvishwanath.bitterskotlin.model.CocktailDbResponse
import com.vivekvishwanath.bitterskotlin.network.CocktailDbServiceWrapper
import com.vivekvishwanath.bitterskotlin.ui.main.state.MainViewState
import com.vivekvishwanath.bitterskotlin.util.ApiSuccessResponse
import com.vivekvishwanath.bitterskotlin.util.DataState
import com.vivekvishwanath.bitterskotlin.util.GenericApiResponse
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@MainScope
class MainRepository @Inject constructor(private val cocktailDbServiceWrapper: CocktailDbServiceWrapper) {

    fun getPopularCocktails(): LiveData<DataState<MainViewState>> =

        object : NetworkBoundResource<CocktailDbResponse, MainViewState>() {

            override fun handleApiSuccessResponse(response: ApiSuccessResponse<CocktailDbResponse>) {
                result.value = DataState.data(data = MainViewState(popularCocktails = response.body.drinks))
            }

            override fun createCall(): LiveData<GenericApiResponse<CocktailDbResponse>> =
                LiveDataReactiveStreams
                    .fromPublisher(
                    cocktailDbServiceWrapper.cocktailDbService.getPopularCocktails()
                        .map { response ->
                            GenericApiResponse.create(response)
                        }
                        .onErrorReturn { throwable ->
                            GenericApiResponse.create(throwable)
                        }
                        .subscribeOn(Schedulers.io()))

        }.asLiveData()
}