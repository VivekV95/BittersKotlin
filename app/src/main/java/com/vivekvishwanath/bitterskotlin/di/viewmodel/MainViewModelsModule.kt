package com.vivekvishwanath.bitterskotlin.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vivekvishwanath.bitterskotlin.di.ViewModelKey
import com.vivekvishwanath.bitterskotlin.di.scope.MainScope
import com.vivekvishwanath.bitterskotlin.ui.main.view.viewmodel.CocktailListViewModel
import com.vivekvishwanath.bitterskotlin.viewmodel.ViewModelProviderFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelsModule {

    @MainScope
    @Binds
    @IntoMap
    @ViewModelKey(CocktailListViewModel::class)
    abstract fun bindMainViewModel(viewModel: CocktailListViewModel): ViewModel

    @MainScope
    @Binds
    abstract fun bindViewModelProviderFactory(
        viewModelProviderFactory: ViewModelProviderFactory
    ): ViewModelProvider.Factory
}