package com.vivekvishwanath.bitterskotlin.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vivekvishwanath.bitterskotlin.model.Cocktail

@Dao
interface CocktailDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllCocktails(cocktails: List<Cocktail>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCocktail(cocktail: Cocktail)

    @Query("SELECT * FROM cocktails")
    fun getCachedCocktailsByType(): LiveData<List<Cocktail>>

    @Query("DELETE FROM cocktails")
    suspend fun deleteCachedCocktailsByType()
}