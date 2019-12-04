package com.vivekvishwanath.bitterskotlin.repository.auth

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.vivekvishwanath.bitterskotlin.ui.auth.AuthState
import com.vivekvishwanath.bitterskotlin.session.SessionManager
import com.vivekvishwanath.bitterskotlin.di.scope.AuthScope
import com.vivekvishwanath.bitterskotlin.repository.JobManager
import com.vivekvishwanath.bitterskotlin.session.SessionState
import com.vivekvishwanath.bitterskotlin.ui.ResponseMessage
import com.vivekvishwanath.bitterskotlin.ui.ResponseType
import com.vivekvishwanath.bitterskotlin.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import javax.inject.Inject

@AuthScope
class AuthRepository @Inject constructor(
    private val sessionManager: SessionManager,
    private val mAuth: FirebaseAuth
) : JobManager("AuthRepository") {

    private lateinit var coroutineScope: CoroutineScope
    private lateinit var job: CompletableJob

    fun registerAccount(email: String, password: String): LiveData<AuthState<SessionState>> {
        sessionManager.setCurrentUser(AuthState.Loading())
        if (isNetworkAvailable()) {
            addJob("registerAccount", initNewJob())
            coroutineScope.launch {
                mAuth
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            signIn(email, password, true)
                        } else {
                            task.exception?.message?.let { message ->
                                sessionManager.setCurrentUser(
                                    AuthState.Error(
                                        ResponseMessage(message, ResponseType.Dialog)
                                    )
                                )
                            }
                        }
                    }
            }
            setTimeoutMessage()
        } else sessionManager.setCurrentUser(
            AuthState.Error(
                ResponseMessage(
                    UNABLE_TODO_OPERATION_WO_INTERNET, ResponseType.Dialog
                )
            )
        )
        return sessionManager.getCurrentUser()
    }

    fun signIn(
        email: String, password: String, isRegistration: Boolean
    ): LiveData<AuthState<SessionState>> {
        sessionManager.setCurrentUser(AuthState.Loading())
        if (isNetworkAvailable()) {
            if (!isRegistration) {
                addJob("signIn", initNewJob())
                setTimeoutMessage()
            }
            coroutineScope.launch {
                mAuth
                    .signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            setSignedInUser()
                        } else
                            task.exception?.message?.let { message ->
                                sessionManager.setCurrentUser(
                                    AuthState.Error(
                                        ResponseMessage(
                                            message,
                                            ResponseType.Dialog
                                        )
                                    )
                                )
                            }
                    }
            }
        } else sessionManager.setCurrentUser(
            AuthState.Error(
                ResponseMessage(
                    UNABLE_TODO_OPERATION_WO_INTERNET, ResponseType.Dialog
                )
            )
        )
        return sessionManager.getCurrentUser()
    }

    fun setSignedInUser(): LiveData<AuthState<SessionState>> {
        sessionManager.setCurrentUser(AuthState.Loading())
        mAuth
            .currentUser
            ?.let { user ->
                if (isNetworkAvailable()) {
                    addJob("setSignedInUser", initNewJob())
                    coroutineScope.launch {
                        user
                            .getIdToken(true)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    task.result?.token?.let { token ->
                                        sessionManager.setCurrentUser(
                                            AuthState.Authenticated(
                                                SessionState(
                                                    user,
                                                    token
                                                )
                                            )
                                        )
                                    }
                                } else
                                    sessionManager.setCurrentUser(
                                        AuthState.Authenticated(
                                            SessionState(
                                                user,
                                                ""
                                            )
                                        )
                                    )
                            }
                    }
                } else
                    sessionManager.setCurrentUser(
                        AuthState.Authenticated(
                            SessionState(
                                user, ""
                            )
                        )
                    )
            }
        return sessionManager.getCurrentUser()
    }

    private fun setTimeoutMessage() {
        GlobalScope.launch(IO) {
            delay(AUTH_TIMEOUT)

            if (!job.isCompleted) {
                sessionManager.setCurrentUser(
                    AuthState.Loading(
                        ResponseMessage(
                            ERROR_LOGIN_TIMEOUT, ResponseType.Toast
                        )
                    )
                )
            }
        }
    }

    @UseExperimental(InternalCoroutinesApi::class)
    private fun initNewJob(): Job {
        if (::job.isInitialized) job.cancel()
        job = Job()
        job.invokeOnCompletion(
            onCancelling = true,
            invokeImmediately = true,
            handler = object : CompletionHandler {
                override fun invoke(cause: Throwable?) {
                    if (job.isCancelled) {
                        Log.d(LOG_TAG, "${this.javaClass.simpleName}: job cancelled")
                    }
                }
            })
        coroutineScope = CoroutineScope(IO + job)
        return job
    }

    private fun isNetworkAvailable(): Boolean =
        sessionManager.isConnectedToTheInternet()


}