package com.vivekvishwanath.bitterskotlin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.vivekvishwanath.bitterskotlin.ui.auth.state.AuthViewState
import com.vivekvishwanath.bitterskotlin.util.AuthState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val mAuth: FirebaseAuth
){

    private val currentUser = MutableLiveData<AuthState<AuthViewState>>()

    fun setCurrentUser(state: AuthState<AuthViewState>) {
        currentUser.value = state
    }

    fun logOut() {
        mAuth.signOut()
        currentUser.value = AuthState.NotAuthenticated()
    }

    fun getCurrentUser(): LiveData<AuthState<AuthViewState>> = currentUser

}