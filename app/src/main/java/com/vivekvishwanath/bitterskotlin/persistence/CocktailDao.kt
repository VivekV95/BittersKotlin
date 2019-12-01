package com.vivekvishwanath.bitterskotlin.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vivekvishwanath.bitterskotlin.model.Cocktail

@Dao
interface CocktailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCocktails(cocktails: List<Cocktail>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCocktail(cocktail: Cocktail)

    @Query("SELECT * FROM cocktails WHERE cache_type = :cacheType")
    fun getCachedCocktailsByType(cacheType: Int): LiveData<List<Cocktail>>
}