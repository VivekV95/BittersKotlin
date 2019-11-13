package com.vivekvishwanath.bitterskotlin.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vivekvishwanath.bitterskotlin.di.ViewModelKey
import com.vivekvishwanath.bitterskotlin.di.scope.AuthScope
import com.vivekvishwanath.bitterskotlin.ui.auth.AuthViewModel
import com.vivekvishwanath.bitterskotlin.viewmodel.ViewModelProviderFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AuthViewModelsModule {

    @AuthScope
    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(viewModel: AuthViewModel): ViewModel

    @AuthScope
    @Binds
    abstract fun bindViewModelProviderFactory
                (viewModelProviderFactory: ViewModelProviderFactory): ViewModelProvider.Factory
}