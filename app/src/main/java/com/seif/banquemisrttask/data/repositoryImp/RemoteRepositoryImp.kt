package com.seif.banquemisrttask.data.repositoryImp

import android.util.Log
import com.seif.banquemisrttask.data.datasources.localdatasource.sharedprefrence.AppSharedPreference
import com.seif.banquemisrttask.data.datasources.remotedatasource.RemoteDataSource
import com.seif.banquemisrttask.data.datasources.remotedatasource.models.TrendingRepositories
import com.seif.banquemisrttask.domain.repository.RemoteRepository
import com.seif.banquemisrttask.util.Constants
import com.seif.banquemisrttask.util.NetworkResult
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped // we will have the same instance even when we rotate screen
class RemoteRepositoryImp @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : RemoteRepository {

    override suspend fun getTrendingRepositories(): NetworkResult<TrendingRepositories>? {

        return remoteDataSource.getTrendingRepositories()
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