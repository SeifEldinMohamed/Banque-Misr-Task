package com.seif.banquemisrttask.data.repositoryImp


import android.net.ConnectivityManager
import android.util.Log
import com.seif.banquemisrttask.data.datasources.localdatasource.LocalDataSource
import com.seif.banquemisrttask.data.datasources.localdatasource.entities.TrendingRepositoriesEntity
import com.seif.banquemisrttask.data.datasources.remotedatasource.RemoteDataSource
import com.seif.banquemisrttask.data.datasources.remotedatasource.models.TrendingRepositoriesItem
import com.seif.banquemisrttask.domain.repository.Repository
import com.seif.banquemisrttask.domain.toTrendingRepositoriesEntityList
import com.seif.banquemisrttask.util.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImp @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val connectivityManager:ConnectivityManager
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
                localDataSource.offlineCacheRepositories(it.toTrendingRepositoriesEntityList()) // mapping to TrendingRepositoriesEntity
            },
            shouldFetch = {
                if(it.isEmpty() || forceFetch){
                    true
                }
                else{
                    Log.d("trending", "should fetch ${it.last().fetchTimeStamp + Constants.TWO_HOURS_INTERVAL <= System.currentTimeMillis()}")
                    it.last().fetchTimeStamp + Constants.TWO_HOURS_INTERVAL < System.currentTimeMillis()
                }
            },
           hasInternetConnection = {
               CommonFunctions.checkInternetConnection(connectivityManager)
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