package com.vivekvishwanath.bitterskotlin.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vivekvishwanath.bitterskotlin.model.CacheType
import com.vivekvishwanath.bitterskotlin.model.Cocktail
import com.vivekvishwanath.bitterskotlin.model.CocktailCacheType

@Database(entities = [Cocktail::class, CacheType::class, CocktailCacheType::class], version = 4, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getCocktailDao(): CocktailDao

    companion object {
        val DATABASE_NAME: String = "app_db"
    }
}