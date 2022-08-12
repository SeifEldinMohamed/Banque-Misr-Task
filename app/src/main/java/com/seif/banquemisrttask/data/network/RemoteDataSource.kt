package com.seif.banquemisrttask.data.network

import android.util.Log
import com.seif.banquemisrttask.data.network.models.TrendingRepositories
import com.seif.banquemisrttask.util.NetworkResult
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val trendingRepositoriesApi: TrendingRepositoriesApi // hilt will search for a function that returns the same type(FoodRecipeApi) in NetworkModule
) {
     suspend fun getTrendingRepositories(): NetworkResult<TrendingRepositories>? {
         val response: Response<TrendingRepositories> = trendingRepositoriesApi.getTrendingRepositories()
         return  handleTrendingRepositoriesResponse(response)
    }

    private fun handleTrendingRepositoriesResponse(response: Response<TrendingRepositories>): NetworkResult<TrendingRepositories>? {
        Log.d("trending", "handle response called")
        return when {
            response.message().toString().contains("timeout") -> NetworkResult.Error("Timeout")
            response.code() == 404 -> NetworkResult.Error("Not Found")
            response.body()
                .isNullOrEmpty() -> NetworkResult.Error("Trending Repositories Not Found.")

            response.isSuccessful -> { // we will return trending repositories from api
                response.body()?.let {
                    // caching data
                  ////  offlineCacheRepositories(it)
                    NetworkResult.Success(it)
                }
            }
            else -> NetworkResult.Error(response.message())
        }
    }

}