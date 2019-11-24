package com.vivekvishwanath.bitterskotlin.ui.auth.state

import com.google.firebase.auth.FirebaseUser

data class AuthViewState(
    var message: String? = null,
    var loginFields: LoginFields? = null,
    var registrationFields: RegistrationFields? = null
)

data class LoginFields(
    val email: String,
    val password: String
)

data class RegistrationFields(
    val email: String,
    val password: String,
    val confirmPassword: String
)