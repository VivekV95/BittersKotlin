package com.vivekvishwanath.bitterskotlin.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.vivekvishwanath.bitterskotlin.BaseApplication
import com.vivekvishwanath.bitterskotlin.R
import com.vivekvishwanath.bitterskotlin.ui.BaseActivity
import com.vivekvishwanath.bitterskotlin.util.*
import com.vivekvishwanath.bitterskotlin.ui.auth.fragment.LoginFragment
import com.vivekvishwanath.bitterskotlin.ui.auth.state.AuthStateEvent
import com.vivekvishwanath.bitterskotlin.ui.main.MainActivity
import com.vivekvishwanath.bitterskotlin.util.AuthState
import com.vivekvishwanath.bitterskotlin.viewmodel.ViewModelProviderFactory
import javax.inject.Inject

class AuthActivity : BaseActivity() {

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
        sessionManager.getCurrentUser().observe(this, Observer { authState ->
            when (authState) {
                is AuthState.Authenticated -> {
                    authState.data?.getContentIfNotHandled()?.let {
                        navToMain()
                    }
                }
                is AuthState.Error -> {
                    authState.message?.getContentIfNotHandled()?.let { responseMessage ->
                        Toast.makeText(this, responseMessage.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    override fun onStop() {
        super.onStop()
        viewModel.cancelJobs()
    }
}
