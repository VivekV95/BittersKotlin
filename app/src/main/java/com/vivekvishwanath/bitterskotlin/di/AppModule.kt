package com.vivekvishwanath.bitterskotlin.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Module
    companion object {

        @Singleton
        @Provides
        @JvmStatic
        fun provideFirebaseAuthInstance() = FirebaseAuth.getInstance()
    }
}