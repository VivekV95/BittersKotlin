package com.vivekvishwanath.bitterskotlin.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.vivekvishwanath.bitterskotlin.BaseApplication
import com.vivekvishwanath.bitterskotlin.R
import com.vivekvishwanath.bitterskotlin.network.CocktailDbServiceWrapper
import com.vivekvishwanath.bitterskotlin.ui.BaseActivity
import com.vivekvishwanath.bitterskotlin.ui.auth.AuthActivity
import com.vivekvishwanath.bitterskotlin.viewmodel.ViewModelProviderFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    val mainComponent by lazy {
        (application as BaseApplication)
            .appComponent
            .mainComponent()
    }

    @Inject
    lateinit var cocktailService: CocktailDbServiceWrapper

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    lateinit var viewModel: MainViewModel

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_popular -> {
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.main_nav_graph, true)
                    .build()
                Navigation
                    .findNavController(this, R.id.main_nav_host_fragment)
                    .navigate(R.id.popularFragment, null, navOptions)
            }
            R.id.nav_filter -> {
                if (isValidDestination(R.id.filterFragment))
                    Navigation
                        .findNavController(this, R.id.main_nav_host_fragment)
                        .navigate(R.id.filterFragment)
            }
            R.id.nav_favorites -> {
                if (isValidDestination(R.id.favoritesFragment))
                    Navigation
                        .findNavController(this, R.id.main_nav_host_fragment)
                        .navigate(R.id.favoritesFragment)
            }
            R.id.nav_logout -> {
                viewModel.logOut()
            }
        }

        menuItem.isChecked = true
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
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

        viewModel = ViewModelProvider(this, viewModelProviderFactory)[MainViewModel::class.java]
        initNav()
    }

    private fun initNav() {
        val navController = Navigation.findNavController(this, R.id.main_nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout)
        NavigationUI.setupWithNavController(nav_view, navController)
        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI
            .navigateUp(
                Navigation.findNavController(this, R.id.main_nav_host_fragment),
                drawer_layout
            )
    }

    override fun navToAuth() {
        Intent(this, AuthActivity::class.java)
            .apply { startActivity(this) }
    }
}
