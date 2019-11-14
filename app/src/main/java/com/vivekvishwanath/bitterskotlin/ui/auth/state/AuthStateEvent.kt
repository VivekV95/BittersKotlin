package com.vivekvishwanath.bitterskotlin.ui.auth.state

sealed class AuthStateEvent {

    data class RegistrationEvent(
        val email: String,
        val password: String
    ): AuthStateEvent()

    data class LoginEvent(
        val email: String,
        val password: String
    ): AuthStateEvent()

    class None: AuthStateEvent()
}