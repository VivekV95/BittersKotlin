package com.vivekvishwanath.bitterskotlin.ui.auth.state

import com.google.firebase.auth.FirebaseUser

data class AuthViewState(
    val message: String? = null,
    val user: FirebaseUser? = null
)