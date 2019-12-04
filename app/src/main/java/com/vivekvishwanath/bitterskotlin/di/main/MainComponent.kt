package com.vivekvishwanath.bitterskotlin.di.main

import com.vivekvishwanath.bitterskotlin.di.scope.MainScope
import com.vivekvishwanath.bitterskotlin.di.viewmodel.MainViewModelsModule
import com.vivekvishwanath.bitterskotlin.ui.main.MainActivity
import com.vivekvishwanath.bitterskotlin.ui.main.view.PopularFragment
import com.vivekvishwanath.bitterskotlin.ui.main.ViewCocktailFragment
import com.vivekvishwanath.bitterskotlin.ui.main.view.CustomFragment
import com.vivekvishwanath.bitterskotlin.ui.main.view.FavoritesFragment
import com.vivekvishwanath.bitterskotlin.ui.main.view.ViewFragment
import dagger.Subcomponent

@MainScope
@Subcomponent(
    modules = [
        MainModule::class,
        MainViewModelsModule::class]
)
interface MainComponent {

    fun inject(mainActivity: MainActivity)

    fun inject(popularFragment: PopularFragment)

    fun inject (favoritesFragment: FavoritesFragment)

    fun inject (customFragment: CustomFragment)

    fun inject (viewFragment: ViewFragment)

    fun inject (viewCocktailFragment: ViewCocktailFragment)

}