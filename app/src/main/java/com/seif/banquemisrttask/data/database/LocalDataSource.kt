package com.seif.banquemisrttask.data.database

import android.util.Log
import com.seif.banquemisrttask.data.database.entities.TrendingRepositoriesEntity
import com.seif.banquemisrttask.data.network.models.TrendingRepositories
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val trendingRepositoriesDao: TrendingRepositoriesDao
) {
     fun readTrendingRepositories(): Flow<List<TrendingRepositoriesEntity>> {
        return trendingRepositoriesDao.readTrendingRepositories()
    }

     suspend fun insertTrendingRepositories(trendingRepositoriesEntity: TrendingRepositoriesEntity) {
        trendingRepositoriesDao.insertTrendingRepositories(trendingRepositoriesEntity)
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun offlineCacheRepositories(trendingRepositories: TrendingRepositories) {
         Log.d("trending", "data cached in database")
         val trendingRepositoriesEntity = TrendingRepositoriesEntity(0, trendingRepositories)
         GlobalScope.launch(Dispatchers.IO) {
             insertTrendingRepositories(trendingRepositoriesEntity)
         }
    }
}