package com.vivekvishwanath.bitterskotlin.di.main

import android.app.Application
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.vivekvishwanath.bitterskotlin.di.scope.MainScope
import com.vivekvishwanath.bitterskotlin.network.CocktailDbService
import com.vivekvishwanath.bitterskotlin.network.CocktailDbServiceWrapper
import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @Module
    companion object {

        @MainScope
        @Provides
        @JvmStatic
        fun provideCocktailDbService(application: Application) = CocktailDbServiceWrapper(application)

        @MainScope
        @Provides
        @JvmStatic
        fun provideGlideInstance(application: Application) = Glide.with(application)

        @MainScope
        @Provides
        @JvmStatic
        fun provideFirebaseUser(firebaseAuth: FirebaseAuth) = firebaseAuth.currentUser

        @MainScope
        @Provides
        @JvmStatic
        fun provideFirebaseDatabaseReference() = FirebaseDatabase.getInstance().reference
    }
}