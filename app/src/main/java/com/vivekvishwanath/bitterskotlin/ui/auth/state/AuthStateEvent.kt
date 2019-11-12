package com.vivekvishwanath.bitterskotlin.ui.auth.state

sealed class AuthStateEvent {

    class RegistrationEvent(
        val email: String,
        val password: String
    ): AuthStateEvent()

    class LoginEvent(
        val email: String,
        val password: String
    ): AuthStateEvent()

    class None: AuthStateEvent()
}