package com.vivekvishwanath.bitterskotlin.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.vivekvishwanath.bitterskotlin.di.scope.MainScope
import com.vivekvishwanath.bitterskotlin.model.Cocktail
import com.vivekvishwanath.bitterskotlin.model.CocktailCacheType
import com.vivekvishwanath.bitterskotlin.persistence.CocktailDao
import com.vivekvishwanath.bitterskotlin.repository.JobManager
import com.vivekvishwanath.bitterskotlin.session.SessionManager
import com.vivekvishwanath.bitterskotlin.ui.Data
import com.vivekvishwanath.bitterskotlin.ui.ResponseMessage
import com.vivekvishwanath.bitterskotlin.ui.ResponseType
import com.vivekvishwanath.bitterskotlin.ui.main.DataState
import com.vivekvishwanath.bitterskotlin.ui.main.view.state.CocktailListViewState
import com.vivekvishwanath.bitterskotlin.ui.main.view.state.CocktailListViewState.*
import com.vivekvishwanath.bitterskotlin.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@MainScope
class FirebaseDatabaseDao @Inject constructor(
    private val firebaseUser: FirebaseUser?,
    private val firebaseDatabase: DatabaseReference,
    private val sessionManager: SessionManager,
    private val cocktailDao: CocktailDao
) : JobManager("FirebaseDatabaseDao") {

    lateinit var job: CompletableJob
    lateinit var coroutineScope: CoroutineScope
    lateinit var idsListener: ValueEventListener

    private val _favoriteCocktailIds = MediatorLiveData<DataState<Set<Int>>>()
    private val favoriteCocktails = MutableLiveData<DataState<CocktailListViewState>>()

    val favoriteCocktailIds: LiveData<DataState<Set<Int>>>
        get() = _favoriteCocktailIds

    fun refreshFavorites() {
        listenToFavoriteCocktailIds()
    }

    private fun listenToFavoriteCocktailIds(): LiveData<DataState<Set<Int>>> {
        firebaseUser?.let {
            if (::idsListener.isInitialized) firebaseDatabase.removeEventListener(idsListener)
            idsListener = getIdsEventListener()
            firebaseDatabase
                .child(FIREBASE_USERS_KEY)
                .child(firebaseUser.uid)
                .child(FIREBASE_FAVORITE_COCKTAILS_KEY)
                .addValueEventListener(idsListener)
        }
        return AbsentLiveData.create()
    }

    private fun getIdsEventListener() =
        object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                refreshFavorites()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //getFavoriteCocktails()
                val cocktailIds = mutableSetOf<Int>()
                dataSnapshot.children.forEach { id ->
                    id.key?.toInt()?.let {
                        cocktailIds.add(it)
                    }
                }
                _favoriteCocktailIds.postValue(DataState.data(cocktailIds))
            }
        }

    /* private fun getFavoriteCocktailsEventListener() =
        object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                favoriteCocktails.value = DataState.error(
                    ResponseMessage(
                        "Something went wront when getting favorite cockatils",
                        ResponseType.Dialog
                    )
                )
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val cocktails = arrayListOf<Cocktail>()
                GlobalScope.launch(IO) {
                    cocktailDao.deleteAllCocktailsByCacheType(CACHE_TYPE_FAVORITES)
                    snapshot.children.forEach { id ->
                        id.getValue(Cocktail::class.java)?.let {
                            launch {
                                val cocktail = it.copy()
                                cocktailDao.insertCocktail(cocktail)
                                cocktailDao.insertCocktailCacheType(
                                    CocktailCacheType(
                                        drinkId = cocktail.drinkId,
                                        cacheTypeId = CACHE_TYPE_FAVORITES
                                    )
                                )
                            }
                            cocktails.add(it)
                        }
                    }
                }
                GlobalScope.launch(Main) {
                    favoriteCocktails.value = DataState.data(
                        CocktailListViewState(
                            CocktailFields(favoriteCocktails = cocktails)
                        )
                    )
                }
            }

        }*/

    suspend fun addFavoriteCocktail(
        cocktail: Cocktail
    ): DataState<String> {
        return suspendCoroutine { cont ->
            if (sessionManager.isConnectedToTheInternet()) {
                addJob("addFavoriteCocktail", initNewJob())
                coroutineScope.launch {
                    //delay(TESTING_NETWORK_DELAY)
                    firebaseUser?.let {
                        firebaseDatabase
                            .child(FIREBASE_USERS_KEY)
                            .child(firebaseUser.uid)
                            .child(FIREBASE_FAVORITE_COCKTAILS_KEY)
                            .child(cocktail.drinkId)
                            .setValue(
                                cocktail
                            ) { error, reference ->
                                error?.let {
                                    cont.resume(
                                        DataState.error(
                                            ResponseMessage(
                                                addFavoriteError(cocktail.drinkName),
                                                ResponseType.Toast
                                            )
                                        )
                                    )
                                } ?: cont.resume(DataState.data("Success"))
                            }
                    }
                }
            } else
                cont.resume(
                    DataState.error(
                        ResponseMessage(
                            UNABLE_TODO_OPERATION_WO_INTERNET,
                            ResponseType.Dialog
                        )
                    )
                )
        }
    }

    suspend fun deleteFavoriteCocktail(
        cocktail: Cocktail
    ): DataState<String> {
        return suspendCoroutine { cont ->
            if (sessionManager.isConnectedToTheInternet()) {
                addJob("deleteFavoriteCocktail", initNewJob())
                coroutineScope.launch {
                    //delay(TESTING_NETWORK_DELAY)
                    firebaseUser?.let {
                        firebaseDatabase
                            .child(FIREBASE_USERS_KEY)
                            .child(firebaseUser.uid)
                            .child(FIREBASE_FAVORITE_COCKTAILS_KEY)
                            .child(cocktail.drinkId)
                            .removeValue { error, reference ->
                                error?.let {
                                    cont.resume(
                                        DataState.error(
                                            ResponseMessage(
                                                deleteFavoriteError(cocktail.drinkName),
                                                ResponseType.Toast
                                            )
                                        )
                                    )
                                } ?: cont.resume(DataState.data("Success"))
                            }
                    }
                }
            } else
                cont.resume(
                    DataState.error(
                        ResponseMessage(
                            UNABLE_TODO_OPERATION_WO_INTERNET,
                            ResponseType.Dialog
                        )
                    )
                )
        }
    }

    @UseExperimental(InternalCoroutinesApi::class)
    private fun initNewJob(): Job {
        Log.d(LOG_TAG, "initNewJob: called.")
        job = Job()
        coroutineScope = CoroutineScope(Dispatchers.Main + job)
        return job
    }

    fun cancelTasks() {
        if (::idsListener.isInitialized) firebaseDatabase.removeEventListener(idsListener)
        cancelActiveJobs()
    }
}