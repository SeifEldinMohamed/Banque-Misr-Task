package com.seif.banquemisrttask.data.datasources.remotedatasource

import com.seif.banquemisrttask.data.datasources.remotedatasource.models.TrendingRepositoriesResponse
import retrofit2.Response
import retrofit2.http.GET

interface TrendingRepositoriesApi {
    @GET("repositories")
    suspend fun getTrendingRepositories(): Response<TrendingRepositoriesResponse>
}