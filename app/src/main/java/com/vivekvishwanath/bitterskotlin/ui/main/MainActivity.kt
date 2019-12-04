package com.vivekvishwanath.bitterskotlin.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vivekvishwanath.bitterskotlin.BaseApplication
import com.vivekvishwanath.bitterskotlin.R
import com.vivekvishwanath.bitterskotlin.ui.BaseActivity
import com.vivekvishwanath.bitterskotlin.ui.main.view.state.CocktailListStateEvent
import androidx.navigation.ui.setupActionBarWithNavController
import com.vivekvishwanath.bitterskotlin.util.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

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
        viewModel.setStateEvent(CocktailListStateEvent.GetFavoriteCocktailsEvent)
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
