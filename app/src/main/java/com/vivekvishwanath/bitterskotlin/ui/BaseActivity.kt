package com.vivekvishwanath.bitterskotlin.ui

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.vivekvishwanath.bitterskotlin.session.SessionManager
import com.vivekvishwanath.bitterskotlin.ui.auth.AuthActivity
import com.vivekvishwanath.bitterskotlin.ui.auth.AuthState
import com.vivekvishwanath.bitterskotlin.ui.main.MainActivity
import com.vivekvishwanath.bitterskotlin.ui.main.DataState
import com.vivekvishwanath.bitterskotlin.ui.main.view.CocktailListViewModel
import com.vivekvishwanath.bitterskotlin.util.LOG_TAG
import com.vivekvishwanath.bitterskotlin.viewmodel.ViewModelProviderFactory
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), DataStateChangedListener {

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    lateinit var viewModel: CocktailListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory)[CocktailListViewModel::class.java]

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


    override fun onDataStateChanged(dataState: DataState<*>?) {
        dataState?.let {
            displayProgressBar(false)
            GlobalScope.launch(Main) {
                displayProgressBar(dataState.loading.isLoading)

                dataState.error?.let { errorEvent ->
                    handleStateError(errorEvent)
                }

                dataState.data?.let { data ->
                    data.responseMessage?.let { responseMessageEvent ->
                        handleStateResponseMessage(responseMessageEvent)
                    }
                }
            }
        }
    }

    private fun handleStateResponseMessage(responseMessageEvent: Event<ResponseMessage>) {
        responseMessageEvent.getContentIfNotHandled()?.let { responseMessage ->
            when (responseMessage.responseType) {
                is ResponseType.Dialog -> {
                    responseMessage.message?.let { message ->
                        displayToast(message)
                    }
                }
                is ResponseType.Toast -> {
                    responseMessage.message?.let { message ->
                        displaySuccessDialog(message)
                    }
                }
                is ResponseType.None -> {
                    Log.d(LOG_TAG, "${this.javaClass.simpleName}: ${responseMessage.message}")
                }
            }
        }
    }

    abstract fun displayProgressBar(isLoading: Boolean)

    private fun handleStateError(errorEvent: Event<StateError>) {
        errorEvent.getContentIfNotHandled()?.let {
            when(it.responseMessage.responseType) {
                is ResponseType.Toast -> {
                    it.responseMessage.message?.let { message ->
                        displayToast(message)
                    }
                }
                is ResponseType.Dialog -> {
                    it.responseMessage.message?.let { message ->
                        displayErrorDialog(message)
                    }
                }
                is ResponseType.None -> {
                    Log.d(LOG_TAG, "${this.javaClass.simpleName}: ${it.responseMessage.message}")
                }
            }
        }
    }

    override fun hideSoftKeyboard() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}