package com.seif.banquemisrttask.data.repositoryImp


import android.net.ConnectivityManager
import android.util.Log
import com.seif.banquemisrttask.data.datasources.localdatasource.LocalDataSource
import com.seif.banquemisrttask.data.datasources.localdatasource.entities.TrendingRepositoriesEntity
import com.seif.banquemisrttask.data.datasources.remotedatasource.RemoteDataSource
import com.seif.banquemisrttask.data.datasources.remotedatasource.dto.TrendingRepositoriesItem
import com.seif.banquemisrttask.domain.model.TrendingRepository
import com.seif.banquemisrttask.domain.repository.Repository
import com.seif.banquemisrttask.domain.toTrendingRepositoriesEntityList
import com.seif.banquemisrttask.domain.toTrendingRepository
import com.seif.banquemisrttask.util.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class RepositoryImp @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val connectivityManager: ConnectivityManager
) : Repository {

    override fun getTrendingRepositories(forceFetch: Boolean): Flow<NetworkResult<List<TrendingRepository>>> {
        return networkBoundResource<List<TrendingRepository>, List<TrendingRepositoriesItem>>( // first: RequestType, second: ResultType
            query = {
               // flowOf(localDataSource.readTrendingRepositories().toTrendingRepository())
                    flow {
                        val reposEntity = localDataSource.readTrendingRepositories().first()
                        emit(reposEntity.toTrendingRepository())
                    }
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
                if (it.isEmpty() || forceFetch) {
                    true
                } else {
                    Log.d(
                        "trending",
                        "should fetch ${it.last().fetchTimeStamp + Constants.TWO_HOURS_INTERVAL <= System.currentTimeMillis()}"
                    )
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