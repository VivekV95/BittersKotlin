package com.vivekvishwanath.bitterskotlin.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.vivekvishwanath.bitterskotlin.BaseApplication
import com.vivekvishwanath.bitterskotlin.R
import com.vivekvishwanath.bitterskotlin.ui.*
import com.vivekvishwanath.bitterskotlin.ui.auth.state.AuthStateEvent
import com.vivekvishwanath.bitterskotlin.ui.main.MainActivity
import com.vivekvishwanath.bitterskotlin.util.LOG_TAG
import com.vivekvishwanath.bitterskotlin.viewmodel.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class AuthActivity : AppCompatActivity(), AuthStateChangedListener {

    override fun onAuthStateChanged(authState: AuthState<*>) {
        if (authState is AuthState.Loading) {
            authState.message?.getContentIfNotHandled()?.let { responseMessage ->
                responseMessage.message?.let { message ->
                    displayToast(message)
                }
            }
        }
    }

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    lateinit var viewModel: AuthViewModel

    val authComponent by lazy {
        (application as BaseApplication)
            .appComponent
            .authComponent()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        authComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        viewModel = ViewModelProvider(this, viewModelProviderFactory)[AuthViewModel::class.java]
        subscribeObservers()
    }


    override fun onStart() {
        super.onStart()
        viewModel.setStateEvent(AuthStateEvent.LoginOnReturnEvent)
    }

    private fun subscribeObservers() {
        viewModel.authState.observe(this, Observer { authState ->
            when (authState) {
                is AuthState.Authenticated -> {
                    authState.data?.getContentIfNotHandled()?.let {
                        navToMain()
                    }
                }
                is AuthState.Error -> {
                    handleResponseMessage(authState.message)
                }
            }
        })
    }

    private fun handleResponseMessage(message: Event<ResponseMessage>?) {
        message?.getContentIfNotHandled()?.let { responseMessage ->
            when (responseMessage.responseType) {
                is ResponseType.Toast -> {
                    responseMessage.message?.let {
                        displayToast(it)
                    }
                }
                is ResponseType.Dialog -> {
                    responseMessage.message?.let {
                        displayErrorDialog(it)
                    }
                }
                is ResponseType.None -> {
                    Log.d(LOG_TAG, "${this.javaClass.simpleName}: Unknown response error")
                }
            }
        }
    }

    private fun navToMain() {
        Intent(this, MainActivity::class.java)
            .apply {
                startActivity(this)
            }
        finish()
    }


    override fun onStop() {
        super.onStop()
        viewModel.cancelJobs()
    }
}
