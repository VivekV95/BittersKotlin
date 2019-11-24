package com.vivekvishwanath.bitterskotlin.ui

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.vivekvishwanath.bitterskotlin.session.SessionManager
import com.vivekvishwanath.bitterskotlin.ui.auth.AuthActivity
import com.vivekvishwanath.bitterskotlin.util.AuthState
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeObservers()
    }

    private fun subscribeObservers() {
        sessionManager.getCurrentUser().observe(this, Observer { authState ->
            when (authState) {
                is AuthState.NotAuthenticated -> {
                   navToAuth()
                }
                is AuthState.Error -> {
                    navToAuth()
                }
            }
        })
    }

    fun navToAuth() {
        Intent(this, AuthActivity::class.java)
            .apply { startActivity(this) }
        finish()
    }
}