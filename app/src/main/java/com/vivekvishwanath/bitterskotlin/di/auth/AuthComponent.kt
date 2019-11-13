package com.vivekvishwanath.bitterskotlin.di.auth

import com.vivekvishwanath.bitterskotlin.di.scope.AuthScope
import com.vivekvishwanath.bitterskotlin.di.viewmodel.AuthViewModelsModule
import com.vivekvishwanath.bitterskotlin.ui.auth.AuthActivity
import com.vivekvishwanath.bitterskotlin.ui.auth.fragment.LoginFragment
import com.vivekvishwanath.bitterskotlin.ui.auth.fragment.RegisterFragment
import dagger.Subcomponent

@AuthScope
@Subcomponent(
    modules = [
    AuthViewModelsModule::class]
)
interface AuthComponent {

    fun inject(authActivity: AuthActivity)

    fun inject(loginFragment: LoginFragment)

    fun inject(registerFragment: RegisterFragment)
}