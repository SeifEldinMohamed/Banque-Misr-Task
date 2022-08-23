package com.seif.banquemisrttask.domain.repository

import com.seif.banquemisrttask.data.datasources.localdatasource.entities.TrendingRepositoriesEntity
import com.seif.banquemisrttask.data.datasources.remotedatasource.models.TrendingRepositoriesResponse
import com.seif.banquemisrttask.util.NetworkResult
import kotlinx.coroutines.flow.Flow

interface Repository {
    // remote data source
    fun getTrendingRepositories(forceFetch:Boolean): Flow<NetworkResult<List<TrendingRepositoriesEntity>>>

    // local data source
    fun sortTrendingRepositoriesByStars(): Flow<List<TrendingRepositoriesEntity>>
    fun sortTrendingRepositoriesByName(): Flow<List<TrendingRepositoriesEntity>>

    }