package com.vivekvishwanath.bitterskotlin.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.vivekvishwanath.bitterskotlin.BaseApplication
import com.vivekvishwanath.bitterskotlin.R
import com.vivekvishwanath.bitterskotlin.network.CocktailDbServiceWrapper
import com.vivekvishwanath.bitterskotlin.network.FirebaseDatabaseDao
import com.vivekvishwanath.bitterskotlin.ui.BaseActivity
import com.vivekvishwanath.bitterskotlin.ui.auth.AuthState
import com.vivekvishwanath.bitterskotlin.ui.main.view.CocktailListFragment
import com.vivekvishwanath.bitterskotlin.ui.main.view.CocktailListViewModel
import com.vivekvishwanath.bitterskotlin.viewmodel.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity() {

    val mainComponent by lazy {
        (application as BaseApplication)
            .appComponent
            .mainComponent()
    }

    @Inject
    lateinit var cocktailService: CocktailDbServiceWrapper

    @Inject
    lateinit var firebaseDatabaseDao: FirebaseDatabaseDao

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    lateinit var viewModel: CocktailListViewModel

    override fun displayProgressBar(isLoading: Boolean) {
        main_progress_bar.isVisible = isLoading
    }


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        mainComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel =
            ViewModelProvider(this, viewModelProviderFactory)[CocktailListViewModel::class.java]

        subscribeObservers()
        testCocktails()

        tool_bar.setOnClickListener {
            sessionManager.logOut()
        }
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

    fun testCocktails() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.main_nav_host_fragment,
                CocktailListFragment()
            )
            .commit()

    }
}
