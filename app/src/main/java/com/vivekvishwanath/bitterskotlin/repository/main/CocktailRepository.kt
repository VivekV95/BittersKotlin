package com.vivekvishwanath.bitterskotlin.repository.main

import android.util.Log
import androidx.lifecycle.LiveData
import com.vivekvishwanath.bitterskotlin.di.scope.MainScope
import com.vivekvishwanath.bitterskotlin.model.Cocktail
import com.vivekvishwanath.bitterskotlin.model.CocktailDbResponse
import com.vivekvishwanath.bitterskotlin.network.CocktailDbServiceWrapper
import com.vivekvishwanath.bitterskotlin.network.FirebaseDatabaseDao
import com.vivekvishwanath.bitterskotlin.repository.JobManager
import com.vivekvishwanath.bitterskotlin.network.NetworkBoundResource
import com.vivekvishwanath.bitterskotlin.session.SessionManager
import com.vivekvishwanath.bitterskotlin.ui.main.view.state.CocktailListViewState
import com.vivekvishwanath.bitterskotlin.util.ApiSuccessResponse
import com.vivekvishwanath.bitterskotlin.ui.main.DataState
import com.vivekvishwanath.bitterskotlin.util.GenericApiResponse
import com.vivekvishwanath.bitterskotlin.util.LOG_TAG
import kotlinx.coroutines.Job
import javax.inject.Inject

@MainScope
class CocktailRepository @Inject constructor(
    private val cocktailDbServiceWrapper: CocktailDbServiceWrapper,
    private val sessionManager: SessionManager,
    private val firebaseDatabaseDao: FirebaseDatabaseDao): JobManager("CocktailRepository") {

    fun getPopularCocktails(): LiveData<DataState<CocktailListViewState>> =

        object : NetworkBoundResource<CocktailDbResponse, CocktailListViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            true,
            false
        ) {
            override fun setJob(job: Job) {
                addJob("getPopularCocktails", job)
            }

            override fun handleApiSuccessResponse(response: ApiSuccessResponse<CocktailDbResponse>) {
                Log.d(LOG_TAG, "${this.javaClass.simpleName}: ${Thread.currentThread()}")
                onCompleteJob(
                    DataState.data(responseMessage = null, data = CocktailListViewState(
                        CocktailListViewState.CocktailFields(popularCocktails = response.body.drinks)
                    ))
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<CocktailDbResponse>> =
                cocktailDbServiceWrapper.cocktailDbService.getPopularCocktails()

        }.asLiveData()

    fun getFavoriteIds() = firebaseDatabaseDao.favoriteCocktailIds

    fun refreshFavorites() {
        firebaseDatabaseDao.refreshFavorites()
    }

    suspend fun addToFavorites(cocktail: Cocktail) = firebaseDatabaseDao.addFavoriteCocktail(cocktail)

    suspend fun deleteFromFavorites(cocktail: Cocktail) = firebaseDatabaseDao.deleteFavoriteCocktail(cocktail)

    fun logOut() {
        sessionManager.logOut()
    }

    fun cancelJobs() {
        cancelActiveJobs()
        firebaseDatabaseDao.cancelTasks()
    }
}