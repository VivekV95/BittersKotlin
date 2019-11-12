package com.vivekvishwanath.bitterskotlin.di.auth

import com.vivekvishwanath.bitterskotlin.ui.auth.AuthActivity
import dagger.Subcomponent

@Subcomponent
interface AuthComponent {

    fun inject(authActivity: AuthActivity)
}