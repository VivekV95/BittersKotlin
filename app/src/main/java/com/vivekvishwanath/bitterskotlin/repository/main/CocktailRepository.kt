package com.vivekvishwanath.bitterskotlin.repository.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.vivekvishwanath.bitterskotlin.di.scope.MainScope
import com.vivekvishwanath.bitterskotlin.model.Cocktail
import com.vivekvishwanath.bitterskotlin.model.CocktailCacheType
import com.vivekvishwanath.bitterskotlin.model.CocktailDbResponse
import com.vivekvishwanath.bitterskotlin.network.CocktailDbServiceWrapper
import com.vivekvishwanath.bitterskotlin.network.FirebaseDatabaseDao
import com.vivekvishwanath.bitterskotlin.network.FirebaseDbServiceWrapper
import com.vivekvishwanath.bitterskotlin.repository.JobManager
import com.vivekvishwanath.bitterskotlin.network.NetworkBoundResource
import com.vivekvishwanath.bitterskotlin.persistence.CocktailDao
import com.vivekvishwanath.bitterskotlin.session.SessionManager
import com.vivekvishwanath.bitterskotlin.ui.ResponseMessage
import com.vivekvishwanath.bitterskotlin.ui.ResponseType
import com.vivekvishwanath.bitterskotlin.ui.main.view.state.CocktailListViewState
import com.vivekvishwanath.bitterskotlin.ui.main.DataState
import com.vivekvishwanath.bitterskotlin.ui.main.view.state.CocktailListViewState.*
import com.vivekvishwanath.bitterskotlin.util.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@MainScope
class CocktailRepository @Inject constructor(
    private val cocktailDbServiceWrapper: CocktailDbServiceWrapper,
    private val sessionManager: SessionManager,
    private val firebaseDatabaseDao: FirebaseDatabaseDao,
    private val cocktailDao: CocktailDao,
    private val firebaseDbServiceWrapper: FirebaseDbServiceWrapper,
    private val gson: Gson
) : JobManager("CocktailRepository") {

    fun getPopularCocktails(): LiveData<DataState<CocktailListViewState>> =

        object : NetworkBoundResource<CocktailDbResponse, List<Cocktail>, CocktailListViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            false,
            true
        ) {
            override suspend fun createCacheRequestAndReturn() {
                // if network is down, get data from cache
                withContext(Main) {
                    result.addSource(loadFromCache()) { viewState ->
                        onCompleteJob(DataState.data(viewState, null))
                    }
                }
            }

            override fun loadFromCache(): LiveData<CocktailListViewState> {
                return cocktailDao
                    .getCachedCocktailsByType(CACHE_TYPE_POPULAR)
                    .switchMap {
                        object : LiveData<CocktailListViewState>() {
                            override fun onActive() {
                                super.onActive()
                                value = CocktailListViewState(
                                    CocktailFields(
                                        popularCocktails = it.sortedBy { cocktail ->
                                            cocktail.drinkName
                                        }
                                    )
                                )
                            }
                        }
                    }
            }

            override suspend fun updateLocalDb(cacheObject: List<Cocktail>?) {
                cacheObject?.let { cocktails ->
                    withContext(IO + job) {
                        cocktails.forEach { cocktail ->
                            try {
                                launch {
                                    cocktailDao.insertCocktail(cocktail)
                                    cocktailDao.insertCocktailCacheType(
                                        CocktailCacheType(
                                            drinkId = cocktail.drinkId,
                                            cacheTypeId = CACHE_TYPE_POPULAR
                                        )
                                    )
                                }
                            } catch (e: Exception) {
                                Log.d(
                                    LOG_TAG, "${this.javaClass.simpleName}:" +
                                            " Error updating cache data for cockatil ${cocktail.drinkName} "
                                )
                            }
                        }
                    }
                }
            }

            override fun setJob(job: Job) {
                addJob("getPopularCocktails", job)
            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<CocktailDbResponse>) {
                Log.d(LOG_TAG, "${this.javaClass.simpleName}: ${Thread.currentThread()}")
                updateLocalDb(response.body.drinks)
                createCacheRequestAndReturn()
            }

            override fun createCall(): LiveData<GenericApiResponse<CocktailDbResponse>> =
                cocktailDbServiceWrapper.cocktailDbService.getPopularCocktails()

        }.asLiveData()

    fun getFavoriteCocktails(): LiveData<DataState<CocktailListViewState>> =

        object : NetworkBoundResource<JsonElement, List<Cocktail>, CocktailListViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            false,
            true
        ) {
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<JsonElement>) {
                val cocktails = arrayListOf<Cocktail>()
                response
                    .body
                    .asJsonObject
                    .get("favoriteCocktails")
                    .asJsonObject
                    .entrySet()
                    .forEach { cocktailJson ->
                        cocktails.add(gson.fromJson(cocktailJson.value, Cocktail::class.java))
                    }
                updateLocalDb(cocktails)
                createCacheRequestAndReturn()
            }

            override fun createCall(): LiveData<GenericApiResponse<JsonElement>>? =
                sessionManager
                    .getCurrentUser()
                    .value
                    ?.data
                    ?.peekContent()
                    ?.let { sessionState ->
                        firebaseDbServiceWrapper
                            .firebaseService
                            .getFavoriteCocktails(
                                sessionState.firebaseUser.uid,
                                sessionState.authToken
                            )
                    }


            override suspend fun createCacheRequestAndReturn() {
                withContext(Main) {
                    result.addSource(loadFromCache()) { viewState ->
                        onCompleteJob(DataState.data(viewState, null))
                    }
                }
            }

            override fun loadFromCache(): LiveData<CocktailListViewState> =
                cocktailDao
                    .getCachedCocktailsByType(CACHE_TYPE_FAVORITES)
                    .switchMap {
                        object : LiveData<CocktailListViewState>() {
                            override fun onActive() {
                                super.onActive()
                                value = CocktailListViewState(
                                    CocktailFields(
                                        favoriteCocktails = it
                                    )
                                )
                            }
                        }
                    }

            override suspend fun updateLocalDb(cacheObject: List<Cocktail>?) {
                cacheObject?.let { cocktails ->
                    withContext(IO + job) {
                        cocktailDao.deleteAllCocktailsByCacheType(CACHE_TYPE_FAVORITES)
                        cocktails.forEach { cocktail ->
                            try {
                                if (cocktailDao.getCountTypesOfCacheForCocktail(cocktail.drinkId) == 0) {
                                    cocktailDao.deleteCocktail(cocktail)
                                }
                                launch {
                                    cocktailDao.insertCocktail(cocktail)
                                    cocktailDao.insertCocktailCacheType(
                                        CocktailCacheType(
                                            drinkId = cocktail.drinkId,
                                            cacheTypeId = CACHE_TYPE_FAVORITES
                                        )
                                    )
                                }
                            } catch (e: Exception) {
                                Log.d(
                                    LOG_TAG, "${this.javaClass.simpleName}:" +
                                            " Error updating cache data for cockatil ${cocktail.drinkName} "
                                )
                            }
                        }
                    }
                }
            }

            override fun setJob(job: Job) {
                addJob("getFavoriteCocktails", job)
            }

        }.asLiveData()

    fun getFavoriteIds() = firebaseDatabaseDao.favoriteCocktailIds

    fun refreshFavorites() {
        firebaseDatabaseDao.refreshFavorites()
    }

    suspend fun addToFavorites(cocktail: Cocktail): DataState<String> {
        return if (sessionManager.isConnectedToTheInternet())
            firebaseDatabaseDao.addFavoriteCocktail(cocktail)
        else
            DataState.error(
                ResponseMessage(
                    message = UNABLE_TODO_OPERATION_WO_INTERNET,
                    responseType = ResponseType.Toast
                )
            )
    }

    suspend fun deleteFromFavorites(cocktail: Cocktail): DataState<String> {
        return if (sessionManager.isConnectedToTheInternet())
            firebaseDatabaseDao.deleteFavoriteCocktail(cocktail)
        else
            DataState.error(
                ResponseMessage(
                    message = UNABLE_TODO_OPERATION_WO_INTERNET,
                    responseType = ResponseType.Toast
                )
            )   
    }

    fun logOut() {
        sessionManager.logOut()
    }

    fun cancelJobs() {
        cancelActiveJobs()
        firebaseDatabaseDao.cancelTasks()
    }
}