package com.seif.banquemisrttask.data.datasources.remotedatasource

import com.seif.banquemisrttask.data.datasources.remotedatasource.models.TrendingRepositoriesItem
import com.seif.banquemisrttask.data.datasources.remotedatasource.models.TrendingRepositoriesResponse
import com.seif.banquemisrttask.util.NetworkResult
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val trendingRepositoriesApi: TrendingRepositoriesApi // hilt will search for a function that returns the same type(FoodRecipeApi) in NetworkModule
) {
     suspend fun getTrendingRepositories(): List<TrendingRepositoriesItem> {
        //  val response: Response<TrendingRepositoriesResponse> = trendingRepositoriesApi.getTrendingRepositories()
         trendingRepositoriesApi.getTrendingRepositories().body()?.let {
             return it
         }
         return emptyList()
    }

//    private fun handleTrendingRepositoriesResponse(response: Response<TrendingRepositoriesResponse>): NetworkResult<TrendingRepositoriesResponse>? {
//        return when {
//            response.message().toString().contains("timeout") -> NetworkResult.Error("Timeout")
//            response.code() == 404 -> NetworkResult.Error("Not Found")
//            response.body()
//                .isNullOrEmpty() -> NetworkResult.Error("Trending Repositories Not Found.")
//
//            response.isSuccessful -> { // we will return trending repositories from api
//                response.body()?.let {
//                    NetworkResult.Success(it)
//                }
//            }
//            else -> NetworkResult.Error(response.message())
//        }
//    }

}