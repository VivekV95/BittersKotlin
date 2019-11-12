package com.vivekvishwanath.bitterskotlin.ui.auth.state

import com.google.firebase.auth.FirebaseUser

data class AuthViewState(
    var message: String? = null,
    var user: FirebaseUser? = null
)