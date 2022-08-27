package com.seif.banquemisrttask.repositories

import com.seif.banquemisrttask.data.datasources.localdatasource.entities.TrendingRepositoriesEntity
import com.seif.banquemisrttask.data.datasources.remotedatasource.models.TrendingRepositoriesItem
import com.seif.banquemisrttask.domain.repository.Repository
import com.seif.banquemisrttask.domain.toTrendingRepositoriesEntityList
import com.seif.banquemisrttask.util.Constants
import com.seif.banquemisrttask.util.NetworkResult
import com.seif.banquemisrttask.util.networkBoundResource
import kotlinx.coroutines.flow.*

class FakeTrendingRepository(
    var reposFromDataBase: MutableList<TrendingRepositoriesEntity> = mutableListOf(),
    var reposFromNetwork: MutableList<TrendingRepositoriesItem> = mutableListOf(),
) : Repository { // make this class to test viewModel

    override fun getTrendingRepositories(forceFetch: Boolean): Flow<NetworkResult<List<TrendingRepositoriesEntity>>> {
        return networkBoundResource<List<TrendingRepositoriesEntity>, List<TrendingRepositoriesItem>>(
            query = {
                flow { emit(reposFromDataBase) }
            },
            fetch = {
                 reposFromNetwork
            },
            saveFetchResult = {
                reposFromDataBase.addAll(it.toTrendingRepositoriesEntityList())
            },
            shouldFetch = {
                if(it.isEmpty() || forceFetch){
                    true
                }
                else{
                    it.last().fetchTimeStamp + Constants.TWO_HOURS_INTERVAL < System.currentTimeMillis()
                }
            }
        )
    }

    override fun sortTrendingRepositoriesByStars(): Flow<List<TrendingRepositoriesEntity>> {
        reposFromDataBase.sortBy {
            it.stars
        }
        return flow { emit(reposFromDataBase) }
    }

    override fun sortTrendingRepositoriesByName(): Flow<List<TrendingRepositoriesEntity>> {
        reposFromDataBase.sortBy {
            it.name
        }
        return flow { emit(reposFromDataBase) }
    }

}

// //    private val trendingRepositoriesItems = mutableListOf<TrendingRepositoriesEntity>()
////
////    private val observeTrendingRepositoriesItem =
////        MutableLiveData<List<TrendingRepositoriesEntity>>(trendingRepositoriesItems)
//
////    private var shouldReturnNetworkError: Boolean = false
////
////    fun setShouldReturnNetworkError(value: Boolean) {
////        shouldReturnNetworkError = value
////    }
////
////    private fun refreshLifeData() {
////        observeTrendingRepositoriesItem.postValue(trendingRepositoriesItems)
////    }


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