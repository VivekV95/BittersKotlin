package com.vivekvishwanath.bitterskotlin.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cache_type")
data class CacheType(

    @PrimaryKey
    @ColumnInfo(name = "cache_type_id")
    val cacheTypeId: Int,

    @ColumnInfo(name = "cache_type")
    val cacheType: String
)