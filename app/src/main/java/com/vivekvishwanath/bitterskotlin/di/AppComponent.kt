package com.vivekvishwanath.bitterskotlin.di

import android.app.Application
import com.vivekvishwanath.bitterskotlin.BaseApplication
import com.vivekvishwanath.bitterskotlin.di.auth.AuthComponent
import com.vivekvishwanath.bitterskotlin.di.main.MainComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class]
)
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun authComponent(): AuthComponent

    fun mainComponent(): MainComponent
}