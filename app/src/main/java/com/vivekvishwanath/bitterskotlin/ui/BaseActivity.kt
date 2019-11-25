package com.vivekvishwanath.bitterskotlin.ui

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.vivekvishwanath.bitterskotlin.session.SessionManager
import com.vivekvishwanath.bitterskotlin.ui.auth.AuthActivity
import com.vivekvishwanath.bitterskotlin.ui.main.MainActivity
import com.vivekvishwanath.bitterskotlin.util.AuthState
import com.vivekvishwanath.bitterskotlin.util.DataState
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), DataStateChangedListener {

    @Inject
    lateinit var sessionManager: SessionManager


    fun navToAuth() {
        Intent(this, AuthActivity::class.java)
            .apply { startActivity(this) }
        finish()
    }

    fun navToMain() {
        Intent(this, MainActivity::class.java)
            .apply {
                startActivity(this)
            }
        finish()
    }

    override fun onDataStateChanged(dataState: DataState<*>) {
        dataState.error?.getContentIfNotHandled()?.let { stateError ->
        //TODO: Display error messages in toasts or dialogs
        }
    }

    override fun hideSoftKeyboard() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}