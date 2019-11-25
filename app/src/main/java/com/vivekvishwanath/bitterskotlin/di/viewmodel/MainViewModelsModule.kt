package com.vivekvishwanath.bitterskotlin.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vivekvishwanath.bitterskotlin.di.ViewModelKey
import com.vivekvishwanath.bitterskotlin.di.scope.MainScope
import com.vivekvishwanath.bitterskotlin.ui.main.CocktailViewModel
import com.vivekvishwanath.bitterskotlin.viewmodel.ViewModelProviderFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelsModule {

    @MainScope
    @Binds
    @IntoMap
    @ViewModelKey(CocktailViewModel::class)
    abstract fun bindMainViewModel(viewModel: CocktailViewModel): ViewModel

    @MainScope
    @Binds
    abstract fun bindViewModelProviderFactory(
        viewModelProviderFactory: ViewModelProviderFactory
    ): ViewModelProvider.Factory
}