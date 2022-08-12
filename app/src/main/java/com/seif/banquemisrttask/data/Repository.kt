package com.seif.banquemisrttask.data

import com.seif.banquemisrttask.data.database.LocalDataSource
import com.seif.banquemisrttask.data.database.entities.TrendingRepositoriesEntity
import com.seif.banquemisrttask.data.network.RemoteDataSource
import com.seif.banquemisrttask.data.network.models.TrendingRepositories
import com.seif.banquemisrttask.data.repositories.Repository
import com.seif.banquemisrttask.util.NetworkResult
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ActivityRetainedScoped // we will have the same instance even when we rotate screen
class Repository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : Repository {
    override fun readTrendingRepositories(): Flow<List<TrendingRepositoriesEntity>> {
        return localDataSource.readTrendingRepositories()
    }

    override suspend fun insertTrendingRepositories(trendingRepositoriesEntity: TrendingRepositoriesEntity) {
        localDataSource.insertTrendingRepositories(trendingRepositoriesEntity)
    }

    override suspend fun getTrendingRepositories(): NetworkResult<TrendingRepositories>? {
        val trendingRepositories = remoteDataSource.getTrendingRepositories()
        if (trendingRepositories is NetworkResult.Success) {
            trendingRepositories.data?.let {
                localDataSource.offlineCacheRepositories(it)
            }
        }
        return trendingRepositories
    }
}