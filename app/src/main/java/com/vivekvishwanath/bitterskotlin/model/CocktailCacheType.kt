package com.vivekvishwanath.bitterskotlin.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["drink_id", "cache_type_id"],
    foreignKeys = [
        ForeignKey(
            entity = Cocktail::class,
            parentColumns = ["drink_id"],
            childColumns = ["drink_id"]
        ),
        ForeignKey(
            entity = CacheType::class,
            parentColumns = ["cache_type_id"],
            childColumns = ["cache_type_id"]
        )],
    tableName = "cocktail_cache_type"
)
data class CocktailCacheType(

    @ColumnInfo(name = "drink_id")
    val drinkId: String,

    @ColumnInfo(name = "cache_type_id", index = true)
    val cacheTypeId: Int
)