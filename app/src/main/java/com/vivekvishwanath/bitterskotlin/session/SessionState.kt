package com.vivekvishwanath.bitterskotlin.session

import com.google.firebase.auth.FirebaseUser

data class SessionState(
    val firebaseUser: FirebaseUser,
    val authToken: String
)