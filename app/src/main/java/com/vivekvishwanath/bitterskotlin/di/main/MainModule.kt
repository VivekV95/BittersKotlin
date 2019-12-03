package com.vivekvishwanath.bitterskotlin.di.main

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.vivekvishwanath.bitterskotlin.di.scope.MainScope
import com.vivekvishwanath.bitterskotlin.network.CocktailDbService
import com.vivekvishwanath.bitterskotlin.network.CocktailDbServiceWrapper
import com.vivekvishwanath.bitterskotlin.network.FirebaseDbService
import com.vivekvishwanath.bitterskotlin.network.FirebaseDbServiceWrapper
import com.vivekvishwanath.bitterskotlin.persistence.AppDatabase
import com.vivekvishwanath.bitterskotlin.persistence.AppDatabase.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
        fun provideFirebaseDbService() = FirebaseDbServiceWrapper()

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
                .addCallback(object: RoomDatabase.Callback() {

                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        GlobalScope.launch(IO) {
                            db.execSQL(
                                "INSERT INTO cache_type (cache_type_id, cache_type) VALUES (1, 'favorites'), (2, 'popular'), (3, 'latest'), (4, 'custom'), (5, 'other')"
                            )
                        }
                    }
                })
                .fallbackToDestructiveMigration()
                .build()

        @MainScope
        @Provides
        @JvmStatic
        fun provideCocktailDao(database: AppDatabase) =
            database.getCocktailDao()
    }
}