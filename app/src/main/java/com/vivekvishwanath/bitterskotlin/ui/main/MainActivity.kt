package com.vivekvishwanath.bitterskotlin.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.vivekvishwanath.bitterskotlin.BaseApplication
import com.vivekvishwanath.bitterskotlin.R
import com.vivekvishwanath.bitterskotlin.network.CocktailDbServiceWrapper
import com.vivekvishwanath.bitterskotlin.ui.BaseActivity
import com.vivekvishwanath.bitterskotlin.ui.auth.AuthState
import com.vivekvishwanath.bitterskotlin.viewmodel.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity()  {

    val mainComponent by lazy {
        (application as BaseApplication)
            .appComponent
            .mainComponent()
    }

    @Inject
    lateinit var cocktailService: CocktailDbServiceWrapper

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    lateinit var viewModel: CocktailViewModel

    override fun displayProgressBar(isLoading: Boolean) {
        main_progress_bar.isVisible = isLoading
    }


    private fun isValidDestination(destination: Int): Boolean =
        destination != Navigation
            .findNavController(this, R.id.main_nav_host_fragment)
            .currentDestination
            ?.id

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        mainComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
      
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[CocktailViewModel::class.java]

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
}
