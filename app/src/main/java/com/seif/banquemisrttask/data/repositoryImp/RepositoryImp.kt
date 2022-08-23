package com.seif.banquemisrttask.data.repositoryImp

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import com.seif.banquemisrttask.data.datasources.localdatasource.LocalDataSource
import com.seif.banquemisrttask.data.datasources.localdatasource.entities.TrendingRepositoriesEntity
import com.seif.banquemisrttask.data.datasources.remotedatasource.RemoteDataSource
import com.seif.banquemisrttask.data.datasources.remotedatasource.models.TrendingRepositoriesItem
import com.seif.banquemisrttask.domain.repository.Repository
import com.seif.banquemisrttask.domain.toTrendingRepositoriesEntityList
import com.seif.banquemisrttask.util.CommonFunctions
import com.seif.banquemisrttask.util.Constants
import com.seif.banquemisrttask.util.NetworkResult
import com.seif.banquemisrttask.util.networkBoundResource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImp(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : Repository {

    override fun getTrendingRepositories(forceFetch:Boolean): Flow<NetworkResult<List<TrendingRepositoriesEntity>>> {
        return networkBoundResource<List<TrendingRepositoriesEntity>, List<TrendingRepositoriesItem>>( // first: RequestType, second: ResultType
            query = {
                localDataSource.readTrendingRepositories()
            },
            fetch = {
                Log.d("trending", "fetch from api")
                remoteDataSource.getTrendingRepositories()
            },
            saveFetchResult = {
                Log.d("trending", "cache data in database")
                localDataSource.offlineCacheRepositories(it.toTrendingRepositoriesEntityList())
            },
            shouldFetch = {
                if(it.isEmpty() || forceFetch){
                    true
                }
                else{
                    Log.d("trending", "should fetch ${it.last().fetchTimeStamp + Constants.TWO_HOURS_INTERVAL <= System.currentTimeMillis()}")
                    it.last().fetchTimeStamp + Constants.TWO_HOURS_INTERVAL < System.currentTimeMillis()
                }
            }
        )
    }


    override fun sortTrendingRepositoriesByStars(): Flow<List<TrendingRepositoriesEntity>> {
        return localDataSource.sortTrendingRepositoriesByStars()
    }

    override fun sortTrendingRepositoriesByName(): Flow<List<TrendingRepositoriesEntity>> {
        return localDataSource.sortTrendingRepositoriesByName()
    }
}