package com.vivekvishwanath.bitterskotlin.repository.main

import androidx.lifecycle.LiveData
import com.vivekvishwanath.bitterskotlin.di.scope.MainScope
import com.vivekvishwanath.bitterskotlin.model.CocktailDbResponse
import com.vivekvishwanath.bitterskotlin.network.CocktailDbServiceWrapper
import com.vivekvishwanath.bitterskotlin.repository.JobManager
import com.vivekvishwanath.bitterskotlin.repository.NetworkBoundResource
import com.vivekvishwanath.bitterskotlin.session.SessionManager
import com.vivekvishwanath.bitterskotlin.ui.main.state.MainViewState
import com.vivekvishwanath.bitterskotlin.util.ApiSuccessResponse
import com.vivekvishwanath.bitterskotlin.ui.main.DataState
import com.vivekvishwanath.bitterskotlin.util.GenericApiResponse
import kotlinx.coroutines.Job
import javax.inject.Inject

@MainScope
class CocktailRepository @Inject constructor(
    private val cocktailDbServiceWrapper: CocktailDbServiceWrapper,
    private val sessionManager: SessionManager): JobManager("CocktailRepository") {

    fun getPopularCocktails(): LiveData<DataState<MainViewState>> =

        object : NetworkBoundResource<CocktailDbResponse, MainViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            true,
            false
        ) {
            override fun setJob(job: Job) {
                addJob("getPopularCocktails", job)
            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<CocktailDbResponse>) {
                onCompleteJob(
                    DataState.data(responseMessage = null, data = MainViewState(response.body.drinks))
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<CocktailDbResponse>> =
                cocktailDbServiceWrapper.cocktailDbService.getPopularCocktails()

        }.asLiveData()

    fun logOut() {
        sessionManager.logOut()
    }

}