package com.vivekvishwanath.bitterskotlin.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vivekvishwanath.bitterskotlin.BaseApplication
import com.vivekvishwanath.bitterskotlin.R
import com.vivekvishwanath.bitterskotlin.network.CocktailDbServiceWrapper
import com.vivekvishwanath.bitterskotlin.network.FirebaseDatabaseDao
import com.vivekvishwanath.bitterskotlin.ui.BaseActivity
import com.vivekvishwanath.bitterskotlin.ui.auth.AuthState
import com.vivekvishwanath.bitterskotlin.ui.main.create.state.SelectIngredientsFragment
import com.vivekvishwanath.bitterskotlin.ui.main.view.CocktailListFragment
import com.vivekvishwanath.bitterskotlin.ui.main.view.CocktailListViewModel
import com.vivekvishwanath.bitterskotlin.ui.main.view.state.CocktailListStateEvent
import com.vivekvishwanath.bitterskotlin.ui.navToAuth
import androidx.navigation.ui.setupActionBarWithNavController
import com.vivekvishwanath.bitterskotlin.util.setupWithNavController
import com.vivekvishwanath.bitterskotlin.viewmodel.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity() {

    private var currentNavController: LiveData<NavController>? = null

    val mainComponent by lazy {
        (application as BaseApplication)
            .appComponent
            .mainComponent()
    }


    override fun displayProgressBar(isLoading: Boolean) {
        main_progress_bar.isVisible = isLoading
    }


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        mainComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(tool_bar)

        tool_bar.setOnClickListener {
            viewModel.setStateEvent(CocktailListStateEvent.GetPopularCocktailsEvent)
        }

        setupBottomNavigationBar()
        viewModel.setStateEvent(CocktailListStateEvent.GetPopularCocktailsEvent)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)

        val navGraphIds = listOf(R.navigation.nav_view, R.navigation.nav_filter, R.navigation.nav_create)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.main_nav_host_fragment,
            intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, Observer { navController ->
            setupActionBarWithNavController(navController)
        })
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    override fun onStart() {
        super.onStart()
        viewModel.refreshFavorites()
    }

    override fun onStop() {
        super.onStop()
        viewModel.cancelActiveJobs()
    }

}
