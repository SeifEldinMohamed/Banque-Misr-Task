package com.seif.banquemisrttask.data.network

import com.seif.banquemisrttask.data.network.models.TrendingRepositories
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val trendingRepositoriesApi: TrendingRepositoriesApi
) { // fetch data from our api

   suspend fun getTrendingRepositories(): Response<TrendingRepositories> {
        return trendingRepositoriesApi.getTrendingRepositories()
    }
}