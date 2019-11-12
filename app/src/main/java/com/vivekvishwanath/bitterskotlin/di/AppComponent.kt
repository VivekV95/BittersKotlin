package com.vivekvishwanath.bitterskotlin.di

import android.app.Application
import com.vivekvishwanath.bitterskotlin.BaseApplication
import dagger.BindsInstance
import dagger.Component

@Component
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}