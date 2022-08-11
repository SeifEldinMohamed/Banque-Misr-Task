package com.seif.banquemisrttask.data

import com.seif.banquemisrttask.data.database.LocalDataSource
import com.seif.banquemisrttask.data.database.TrendingRepositoriesDao
import com.seif.banquemisrttask.data.database.entities.TrendingRepositoriesEntity
import com.seif.banquemisrttask.data.network.RemoteDataSource
import com.seif.banquemisrttask.data.network.TrendingRepositoriesApi
import com.seif.banquemisrttask.data.network.models.TrendingRepositories
import com.seif.banquemisrttask.data.repositories.Repository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

@ActivityRetainedScoped // we will have the same instance even when we rotate screen
class Repository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
    ): Repository {
    override fun readTrendingRepositories(): Flow<List<TrendingRepositoriesEntity>> {
        return localDataSource.readTrendingRepositories()
    }

    override suspend fun insertTrendingRepositories(trendingRepositoriesEntity: TrendingRepositoriesEntity) {
        localDataSource.insertTrendingRepositories(trendingRepositoriesEntity)
    }

    override suspend fun getTrendingRepositories(): Response<TrendingRepositories> {
        return remoteDataSource.getTrendingRepositories()
    }

}