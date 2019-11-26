package com.vivekvishwanath.bitterskotlin.ui.auth

interface AuthStateChangedListener {
    fun onAuthStateChanged(authState: AuthState<*>)
}