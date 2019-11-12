package com.vivekvishwanath.bitterskotlin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val mAuth: FirebaseAuth
){

    private val currentUser = MutableLiveData<AuthState<FirebaseUser>>()

    fun setCurrentUser(state: AuthState<FirebaseUser>) {
        currentUser.value = state
    }

    fun logOut() {
        mAuth.signOut()
        currentUser.value = AuthState.NotAuthenticated()
    }

    fun getCurrentUser(): LiveData<AuthState<FirebaseUser>> = currentUser

}