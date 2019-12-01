package com.vivekvishwanath.bitterskotlin.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vivekvishwanath.bitterskotlin.model.Cocktail

@Database(entities = [Cocktail::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getCocktailDao(): CocktailDao

    companion object {
        val DATABASE_NAME: String = "app_db"
    }
}