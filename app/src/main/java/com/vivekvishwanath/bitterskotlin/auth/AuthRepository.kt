package com.vivekvishwanath.bitterskotlin.auth

import com.google.firebase.auth.FirebaseAuth
import com.vivekvishwanath.bitterskotlin.AuthState
import com.vivekvishwanath.bitterskotlin.SessionManager
import javax.inject.Inject


class AuthRepository @Inject constructor(
    private val sessionManager: SessionManager,
    private val mAuth: FirebaseAuth
) {

    fun registerAccount(email: String, password: String) {
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

    }

    fun signIn(email: String, password: String) {
        sessionManager.setCurrentUser(AuthState.Loading())
        mAuth
        .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mAuth.currentUser?.let { firebaseUser ->
                        sessionManager.setCurrentUser(AuthState.Authenticated(firebaseUser))
                    }
                }
            }
    }
}