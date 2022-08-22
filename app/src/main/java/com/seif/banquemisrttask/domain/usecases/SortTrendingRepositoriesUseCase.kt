package com.seif.banquemisrttask.domain.usecases

import com.seif.banquemisrttask.data.datasources.localdatasource.entities.TrendingRepositoriesEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SortTrendingRepositoriesUseCase(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun sortTrendingRepositoriesByStars(trendingRepositoriesEntity: ArrayList<TrendingRepositoriesEntity>) = withContext(defaultDispatcher) {
        trendingRepositoriesEntity[0].trendingRepositories.sortedBy { item ->
            item.stars
        }.toCollection(ArrayList())
    }

    suspend fun sortTrendingRepositoriesByName(trendingRepositoriesEntity: ArrayList<TrendingRepositoriesEntity>) = withContext(defaultDispatcher) {
        trendingRepositoriesEntity[0].trendingRepositories.sortedBy { item ->
            item.name
        }.toCollection(ArrayList())
    }
}