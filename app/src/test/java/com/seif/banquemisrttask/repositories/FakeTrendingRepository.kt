package com.seif.banquemisrttask.repositories

import androidx.lifecycle.MutableLiveData
import com.seif.banquemisrttask.data.datasources.localdatasource.entities.TrendingRepositoriesEntity
import com.seif.banquemisrttask.data.datasources.remotedatasource.models.TrendingRepositoriesResponse
import com.seif.banquemisrttask.data.datasources.remotedatasource.models.TrendingRepositoriesItem
import com.seif.banquemisrttask.util.NetworkResult
import org.apache.maven.artifact.ant.RemoteRepository


class FakeTrendingRepository : RemoteRepository() { // make this class to test viewModel
    private val trendingRepositoriesItems = mutableListOf<TrendingRepositoriesEntity>()

    private val observeTrendingRepositoriesItem =
        MutableLiveData<List<TrendingRepositoriesEntity>>(trendingRepositoriesItems)

    private var shouldReturnNetworkError: Boolean = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    private fun refreshLifeData() {
        observeTrendingRepositoriesItem.postValue(trendingRepositoriesItems)
    }


//    override suspend fun getTrendingRepositories(): NetworkResult<TrendingRepositoriesResponse> {
//        return if(shouldReturnNetworkError){ // if we want to return an error
//            NetworkResult.Error("error")
//        }
//        else{
//            val trendingRepositoriesList = arrayListOf<TrendingRepositoriesItem>(
//                TrendingRepositoriesItem(
//                    "seif",
//                    "https://picsum.photos/seed/picsum/200/300",
//                    "hello, this seif's repository",
//                    1000,
//                    "Kotlin",
//                    "#000000",
//                    "My Kotlin Repository",
//                    2000,
//                    "https://picsum.photos/seed/picsum/200/300"
//                ))
//            val trendingRepositoriesResponse = TrendingRepositoriesResponse()
//            trendingRepositoriesResponse.addAll(trendingRepositoriesList)
//
//            NetworkResult.Success(trendingRepositoriesResponse)
//        }
//    }
//
//    override fun shouldFetchData(): Boolean {
//        return true
//    }

 }