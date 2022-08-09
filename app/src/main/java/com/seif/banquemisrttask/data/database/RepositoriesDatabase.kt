package com.seif.banquemisrttask.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.seif.banquemisrttask.data.database.entities.TrendingRepositoriesEntity

@Database(
    entities = [TrendingRepositoriesEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(RepositoriesTypeConverter::class)
abstract class RepositoriesDatabase: RoomDatabase() {
    abstract fun trendingRepositoriesDao(): TrendingRepositoriesDao

}