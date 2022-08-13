package com.seif.banquemisrttask.data

import android.util.Log
import com.seif.banquemisrttask.data.database.LocalDataSource
import com.seif.banquemisrttask.data.database.entities.TrendingRepositoriesEntity
import com.seif.banquemisrttask.data.database.sharedprefrence.AppSharedPreference
import com.seif.banquemisrttask.data.network.RemoteDataSource
import com.seif.banquemisrttask.data.network.models.TrendingRepositories
import com.seif.banquemisrttask.data.repositories.Repository
import com.seif.banquemisrttask.util.Constants
import com.seif.banquemisrttask.util.NetworkResult
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ActivityRetainedScoped // we will have the same instance even when we rotate screen
class DefaultRepository @Inject constructor(
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

    override fun shouldFetchData(): Boolean {
        val lastTimeDataFetched = AppSharedPreference.readLastTimeDataFetched("fetchTime", 0L)
        Log.d("trending", "last time data fetched $lastTimeDataFetched")
        Log.d("trending", "current time  ${System.currentTimeMillis()}")
        lastTimeDataFetched?.let {
            Log.d(
                "trending",
                "should fetch ${it + Constants.TWO_HOURS_INTERVAL <= System.currentTimeMillis()}"
            )
            return it + Constants.TWO_HOURS_INTERVAL <= System.currentTimeMillis()
        }
        return false
    }
}