package com.seif.banquemisrttask.data.datasources.localdatasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.seif.banquemisrttask.data.datasources.localdatasource.entities.TrendingRepositoriesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrendingRepositoriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrendingRepositories(trendingRepositoriesEntity: List<TrendingRepositoriesEntity>)

    @Query("SELECT * FROM TRENDING_TABLE")
    fun readTrendingRepositories(): Flow<List<TrendingRepositoriesEntity>>

    @Query("SELECT * FROM TRENDING_TABLE order by name")
    fun sortTrendingRepositoriesByName(): Flow<List<TrendingRepositoriesEntity>>

    @Query("SELECT * FROM TRENDING_TABLE order by stars")
    fun sortTrendingRepositoriesByStars(): Flow<List<TrendingRepositoriesEntity>>

}