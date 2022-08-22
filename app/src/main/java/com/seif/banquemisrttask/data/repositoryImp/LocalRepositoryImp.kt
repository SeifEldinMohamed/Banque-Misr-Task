package com.seif.banquemisrttask.data.repositoryImp

import com.seif.banquemisrttask.data.datasources.localdatasource.LocalDataSource
import com.seif.banquemisrttask.data.datasources.localdatasource.entities.TrendingRepositoriesEntity
import com.seif.banquemisrttask.data.datasources.remotedatasource.models.TrendingRepositories
import com.seif.banquemisrttask.domain.repository.LocalRepository
import kotlinx.coroutines.flow.Flow

class LocalRepositoryImp(
    private val localDataSource: LocalDataSource
    ): LocalRepository {
    override fun readTrendingRepositories(): Flow<List<TrendingRepositoriesEntity>> {
        return localDataSource.readTrendingRepositories()
    }

    override suspend fun insertTrendingRepositories(trendingRepositoriesEntity: TrendingRepositoriesEntity) {
        localDataSource.insertTrendingRepositories(trendingRepositoriesEntity)
    }

    override suspend fun offlineCacheRepositories(trendingRepositories: TrendingRepositories) {
            localDataSource.offlineCacheRepositories(trendingRepositories)
    }
}