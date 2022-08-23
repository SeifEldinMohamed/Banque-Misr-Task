package com.seif.banquemisrttask.data.datasources.localdatasource

import android.util.Log
import com.seif.banquemisrttask.data.datasources.localdatasource.entities.TrendingRepositoriesEntity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val trendingRepositoriesDao: TrendingRepositoriesDao
) {
    fun readTrendingRepositories(): Flow<List<TrendingRepositoriesEntity>> {
        return trendingRepositoriesDao.readTrendingRepositories()
    }

    fun sortTrendingRepositoriesByName(): Flow<List<TrendingRepositoriesEntity>> {
        return trendingRepositoriesDao.sortTrendingRepositoriesByName()
    }

    fun sortTrendingRepositoriesByStars(): Flow<List<TrendingRepositoriesEntity>> {
        return trendingRepositoriesDao.sortTrendingRepositoriesByStars()
    }


    suspend fun offlineCacheRepositories(trendingRepositoriesEntityList: List<TrendingRepositoriesEntity>) {
        withContext(Dispatchers.IO) {
            Log.d("trending", "cached time: ${trendingRepositoriesEntityList.last().fetchTimeStamp}")
            insertTrendingRepositories(trendingRepositoriesEntityList)
        }
    }

    private suspend fun insertTrendingRepositories(trendingRepositories: List<TrendingRepositoriesEntity>) {
        trendingRepositoriesDao.insertTrendingRepositories(trendingRepositories)
    }

}

// The withContext function is similar to coroutineScope, but it additionally allows some changes
// to be made to the scope. The context provided as an argument to this function overrides the context
// from the parent scope (the same way as in coroutine builders). This means that
// withContext(EmptyCoroutineContext) and coroutineScope() behave in exactly the same way.

// coroutineScope is a suspending function that starts a scope. It returns the value produced by the argument function.
// Unlike async or launch, the body of coroutineScope is called in-place. It formally creates a new coroutine,
// but it suspends the previous one until the new one is finished, so it does not start any concurrent
// process. Take a look at the below example, in which both delay calls suspend runBlocking.

//        coroutineScope {
//            insertTrendingRepositories(trendingRepositoriesEntity)
//        }

//        GlobalScope.launch(Dispatchers.IO) {
//            insertTrendingRepositories(trendingRepositoriesEntity)
//        }