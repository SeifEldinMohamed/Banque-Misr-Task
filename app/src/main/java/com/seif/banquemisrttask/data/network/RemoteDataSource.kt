package com.seif.banquemisrttask.data.network

import com.seif.banquemisrttask.data.network.models.TrendingRepositories
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val trendingRepositoriesApi: TrendingRepositoriesApi // hilt will search for a function that returns the same type(FoodRecipeApi) in NetworkModule
) {
     suspend fun getTrendingRepositories(): Response<TrendingRepositories> {
        return trendingRepositoriesApi.getTrendingRepositories()
    }
}