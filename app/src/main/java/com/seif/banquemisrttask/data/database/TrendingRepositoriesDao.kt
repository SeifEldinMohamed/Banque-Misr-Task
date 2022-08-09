package com.seif.banquemisrttask.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.seif.banquemisrttask.data.database.entities.TrendingRepositoriesEntity
import com.seif.banquemisrttask.data.network.models.TrendingRepositories
import kotlinx.coroutines.flow.Flow

@Dao
interface TrendingRepositoriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrendingRepositories(trendingRepositoriesEntity: TrendingRepositoriesEntity)

    @Query("SELECT * FROM TRENDING_TABLE ORDER BY id")
    fun readTrendingRepositories(): Flow<List<TrendingRepositoriesEntity>>
}