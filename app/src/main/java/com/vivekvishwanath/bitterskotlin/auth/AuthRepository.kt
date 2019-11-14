package com.vivekvishwanath.bitterskotlin.auth

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.vivekvishwanath.bitterskotlin.util.AuthState
import com.vivekvishwanath.bitterskotlin.SessionManager
import com.vivekvishwanath.bitterskotlin.di.scope.AuthScope
import com.vivekvishwanath.bitterskotlin.ui.auth.state.AuthViewState
import javax.inject.Inject

@AuthScope
class AuthRepository @Inject constructor(
    private val sessionManager: SessionManager,
    private val mAuth: FirebaseAuth
) {

    fun registerAccount(email: String, password: String): LiveData<AuthState<AuthViewState>> {
        sessionManager.setCurrentUser(AuthState.Loading())
        mAuth
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    signIn(email, password)
                } else {
                    task.exception?.message?.let { message ->
                        sessionManager.setCurrentUser(AuthState.Error(message))
                    }
                }
            }
        return sessionManager.getCurrentUser()
    }

    fun signIn(email: String, password: String): LiveData<AuthState<AuthViewState>> {
        sessionManager.setCurrentUser(AuthState.Loading())
        mAuth
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    setSignedInUser()
                else
                    task.exception?.message?.let { message ->
                        sessionManager.setCurrentUser(AuthState.Error(message))

                    }
            }
        return sessionManager.getCurrentUser()
    }

    fun setSignedInUser(): LiveData<AuthState<AuthViewState>> {
        sessionManager.setCurrentUser(AuthState.Loading())
        mAuth.currentUser?.let { firebaseUser ->
            sessionManager.setCurrentUser(AuthState.Authenticated(AuthViewState(user = firebaseUser)))
        } ?: sessionManager.setCurrentUser(AuthState.NotAuthenticated())
        return sessionManager.getCurrentUser()
    }
}