package com.vivekvishwanath.bitterskotlin.di.main

import com.vivekvishwanath.bitterskotlin.di.scope.MainScope
import com.vivekvishwanath.bitterskotlin.ui.main.MainActivity
import dagger.Component
import dagger.Subcomponent

@MainScope
@Subcomponent(
    modules = [
        MainModule::class]
)
interface MainComponent {

    fun inject(mainActivity: MainActivity)
}