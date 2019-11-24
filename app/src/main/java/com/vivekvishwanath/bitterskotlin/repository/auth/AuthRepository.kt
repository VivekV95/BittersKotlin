package com.vivekvishwanath.bitterskotlin.repository.auth

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.vivekvishwanath.bitterskotlin.util.AuthState
import com.vivekvishwanath.bitterskotlin.session.SessionManager
import com.vivekvishwanath.bitterskotlin.di.scope.AuthScope
import com.vivekvishwanath.bitterskotlin.ui.auth.state.AuthViewState
import com.vivekvishwanath.bitterskotlin.util.TAG
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import javax.inject.Inject

@AuthScope
class AuthRepository @Inject constructor(
    private val sessionManager: SessionManager,
    private val mAuth: FirebaseAuth
) {

    private lateinit var coroutineScope: CoroutineScope
    private lateinit var job: Job

    fun registerAccount(email: String, password: String): LiveData<AuthState<AuthViewState>> {
        sessionManager.setCurrentUser(AuthState.Loading())
        initNewJob()
        coroutineScope.launch {
            mAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        signIn(email, password, true)
                    } else {
                        task.exception?.message?.let { message ->
                            sessionManager.setCurrentUser(AuthState.Error(message))
                        }
                    }
                }
        }
        return sessionManager.getCurrentUser()
    }

    fun signIn(
        email: String, password: String, isRegistration: Boolean
    ): LiveData<AuthState<AuthViewState>> {
        sessionManager.setCurrentUser(AuthState.Loading())
        if (!isRegistration)
            initNewJob()
        coroutineScope.launch {
            mAuth
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        setSignedInUser()
                    } else
                        task.exception?.message?.let { message ->
                            sessionManager.setCurrentUser(AuthState.Error(message))
                        }
                }
        }
        return sessionManager.getCurrentUser()
    }

    fun setSignedInUser(): LiveData<AuthState<AuthViewState>> {
        mAuth.currentUser?.let { firebaseUser ->
            sessionManager.setCurrentUser(AuthState.Authenticated(AuthViewState(user = firebaseUser)))
        }
        return sessionManager.getCurrentUser()
    }

    @UseExperimental(InternalCoroutinesApi::class)
    private fun initNewJob(): Job {
        job = if (::job.isInitialized && job.isActive) {
            job.cancel()
            Job()
        } else {
            Job()
        }
        job.invokeOnCompletion(
            onCancelling = true,
            invokeImmediately = true,
            handler = object: CompletionHandler {
                override fun invoke(cause: Throwable?) {
                    Log.d(TAG, "${this.javaClass.simpleName}: job cancelled")
                }
            })
        coroutineScope = CoroutineScope(IO + job)
        return job
    }

    fun cancelJob() {
        if (::job.isInitialized) {
            job.cancel()
        }
    }
}