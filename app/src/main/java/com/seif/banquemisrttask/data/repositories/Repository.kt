package com.seif.banquemisrttask.data.repositories

import com.seif.banquemisrttask.data.database.entities.TrendingRepositoriesEntity
import com.seif.banquemisrttask.data.network.models.TrendingRepositories
import com.seif.banquemisrttask.util.NetworkResult
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface Repository {
    // local (ROOM)
    fun readTrendingRepositories(): Flow<List<TrendingRepositoriesEntity>>
    suspend fun insertTrendingRepositories(trendingRepositoriesEntity: TrendingRepositoriesEntity)

    // Remote (Api)
    suspend fun getTrendingRepositories(): NetworkResult<TrendingRepositories>?

}