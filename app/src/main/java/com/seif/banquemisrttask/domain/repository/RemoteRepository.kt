package com.seif.banquemisrttask.domain.repository

import com.seif.banquemisrttask.data.datasources.localdatasource.entities.TrendingRepositoriesEntity
import com.seif.banquemisrttask.data.datasources.remotedatasource.models.TrendingRepositories
import com.seif.banquemisrttask.util.NetworkResult
import kotlinx.coroutines.flow.Flow

interface RemoteRepository {
    suspend fun getTrendingRepositories(): NetworkResult<TrendingRepositories>?
    fun shouldFetchData(): Boolean

}