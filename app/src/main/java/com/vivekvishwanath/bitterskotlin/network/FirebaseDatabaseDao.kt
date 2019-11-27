package com.vivekvishwanath.bitterskotlin.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.vivekvishwanath.bitterskotlin.di.scope.MainScope
import com.vivekvishwanath.bitterskotlin.repository.JobManager
import com.vivekvishwanath.bitterskotlin.session.SessionManager
import com.vivekvishwanath.bitterskotlin.ui.ResponseMessage
import com.vivekvishwanath.bitterskotlin.ui.ResponseType
import com.vivekvishwanath.bitterskotlin.ui.main.DataState
import com.vivekvishwanath.bitterskotlin.ui.main.view.state.CocktailListViewState
import com.vivekvishwanath.bitterskotlin.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import javax.inject.Inject

@MainScope
class FirebaseDatabaseDao @Inject constructor(
    private val firebaseUser: FirebaseUser?,
    private val firebaseDatabase: DatabaseReference,
    private val sessionManager: SessionManager
) : JobManager("FirebaseDatabaseDao") {

    private val _favoriteCocktails = MediatorLiveData<DataState<ArrayList<Int>>>()

    val favoriteCocktailIds: LiveData<DataState<ArrayList<Int>>>
        get() {
            val source = listenToFavoriteCocktailIds()
            _favoriteCocktails.addSource(source) { dataState ->
                _favoriteCocktails.value = dataState
                if (!dataState.loading.isLoading)
                    _favoriteCocktails.removeSource(source)
            }
            return _favoriteCocktails
        }

    private fun listenToFavoriteCocktailIds(): LiveData<DataState<ArrayList<Int>>> {
        return object : NetworkBoundResource<Void, ArrayList<Int>>(
            sessionManager.isConnectedToTheInternet(),
            true,
            true,
            false
        ) {
            override fun handleApiSuccessResponse(response: ApiSuccessResponse<Void>) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun createCall(): LiveData<GenericApiResponse<Void>>? {
                firebaseUser?.let { user ->
                    firebaseDatabase
                        .child(FIREBASE_USERS_KEY)
                        .child(user.uid)
                        .child(FIREBASE_FAVORITE_IDS_KEY)
                        .addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(error: DatabaseError) {
                               onReturnError(error.message)
                            }

                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val ids = arrayListOf<Int>()
                                dataSnapshot.children.forEach { id ->
                                    id.getValue(String::class.java)?.let {
                                        ids.add(it.toInt())
                                    }
                                }
                                result.value = DataState.data(ids)
                            }
                        })
                }
                return null
            }

            override fun setJob(job: Job) {
                addJob("listenToFavoriteCocktailIds", job)
            }

        }.asLiveData()
    }

    fun cancelJobs() {
        cancelActiveJobs()
    }
}