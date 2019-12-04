package com.vivekvishwanath.bitterskotlin.di.main

import com.vivekvishwanath.bitterskotlin.di.scope.MainScope
import com.vivekvishwanath.bitterskotlin.di.viewmodel.MainViewModelsModule
import com.vivekvishwanath.bitterskotlin.ui.main.MainActivity
import com.vivekvishwanath.bitterskotlin.ui.main.view.PopularFragment
import com.vivekvishwanath.bitterskotlin.ui.main.ViewCocktailFragment
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

    fun inject (viewCocktailFragment: ViewCocktailFragment)
}