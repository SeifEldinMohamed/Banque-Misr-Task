package com.seif.banquemisrttask.data.network

import com.seif.banquemisrttask.data.database.entities.TrendingRepositories
import retrofit2.Response
import retrofit2.http.GET

interface TrendingRepositoriesApi {
    @GET("repositories")
    suspend fun getTrendingRepositories(): Response<TrendingRepositories>
}