package com.seif.banquemisrttask.data.database

import com.seif.banquemisrttask.data.database.entities.TrendingRepositoriesEntity
import kotlinx.coroutines.flow.Flow
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
}