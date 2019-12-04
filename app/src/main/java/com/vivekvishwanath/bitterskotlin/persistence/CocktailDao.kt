package com.vivekvishwanath.bitterskotlin.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.vivekvishwanath.bitterskotlin.model.Cocktail
import com.vivekvishwanath.bitterskotlin.model.CocktailCacheType

@Dao
interface CocktailDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllCocktails(cocktails: List<Cocktail>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCocktail(cocktail: Cocktail)

    @Delete
    suspend fun deleteCocktail(cocktail: Cocktail)

    @Query("""
        SELECT * FROM cocktails
        INNER JOIN cocktail_cache_type
        ON cocktails.drink_id = cocktail_cache_type.drink_id
        WHERE cocktail_cache_type.cache_type_id = :cacheTypeId
    """)
    fun getCachedCocktailsByType(cacheTypeId: Int): LiveData<List<Cocktail>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCocktailCacheType(cocktailCacheType: CocktailCacheType)

    @Delete
    suspend fun deleteCocktailCacheType(cocktailCacheType: CocktailCacheType)

    @Query("""
        DELETE FROM cocktail_cache_type
        WHERE cache_type_id = :cacheTypeId
    """)
    suspend fun deleteAllCocktailsByCacheType(cacheTypeId: Int)

    @Query("""
        SELECT COUNT(*)
        FROM cocktail_cache_type
        WHERE drink_id = :cocktailId
    """)
    fun getCountTypesOfCacheForCocktail(cocktailId: String): Int

}