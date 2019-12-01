package com.vivekvishwanath.bitterskotlin.di.main

import android.app.Application
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.vivekvishwanath.bitterskotlin.di.scope.MainScope
import com.vivekvishwanath.bitterskotlin.network.CocktailDbService
import com.vivekvishwanath.bitterskotlin.network.CocktailDbServiceWrapper
import com.vivekvishwanath.bitterskotlin.persistence.AppDatabase
import com.vivekvishwanath.bitterskotlin.persistence.AppDatabase.Companion.DATABASE_NAME
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
        fun providePicassoInstance(application: Application) = Picasso.get()

        @MainScope
        @Provides
        @JvmStatic
        fun provideFirebaseUser(firebaseAuth: FirebaseAuth) = firebaseAuth.currentUser

        @MainScope
        @Provides
        @JvmStatic
        fun provideFirebaseDatabaseReference() = FirebaseDatabase.getInstance().reference

        @MainScope
        @Provides
        @JvmStatic
        fun provideAppDatabase(application: Application) =
            Room
                .databaseBuilder(application, AppDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()

        @MainScope
        @Provides
        @JvmStatic
        fun provideCocktailDao(database: AppDatabase) =
            database.getCocktailDao()
    }
}