package com.seif.banquemisrttask.repositories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.seif.banquemisrttask.data.database.entities.TrendingRepositoriesEntity
import com.seif.banquemisrttask.data.network.models.TrendingRepositories
import com.seif.banquemisrttask.data.network.models.TrendingRepositoriesItem
import com.seif.banquemisrttask.data.repositories.Repository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


class FakeTrendingRepository: Repository { // make this class to test viewModel
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

    override fun readTrendingRepositories(): Flow<List<TrendingRepositoriesEntity>> {
        return observeTrendingRepositoriesItem.asFlow()
    }

    override suspend fun insertTrendingRepositories(trendingRepositoriesEntity: TrendingRepositoriesEntity) {
        trendingRepositoriesItems.add(trendingRepositoriesEntity)
        refreshLifeData()
    }

    override suspend fun getTrendingRepositories(): Response<TrendingRepositories> {

        val trendingRepositoriesList = arrayListOf<TrendingRepositoriesItem>(
                TrendingRepositoriesItem(
                    "seif",
                    "https://picsum.photos/seed/picsum/200/300",
                    "hello, this seif's repository",
                    1000,
                    "Kotlin",
                    "#000000",
                    "My Kotlin Repository",
                    2000,
                    "https://picsum.photos/seed/picsum/200/300"
                ))
            val trendingRepositories = TrendingRepositories()
            trendingRepositories.addAll(trendingRepositoriesList)

        return Response.success(trendingRepositories)
    }

//    // remote (Api)
//    suspend fun getTrendingRepositories(): NetworkResult<TrendingRepositories> {
//
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
//            val trendingRepositories = TrendingRepositories()
//            trendingRepositories.addAll(trendingRepositoriesList)
//
//            NetworkResult.Success(trendingRepositories)
//        }
//    }
//
//    // local (ROOM)
//    fun readTrendingRepositories(): Flow<List<TrendingRepositoriesEntity>> {
//        return observeTrendingRepositoriesItem.asFlow()
//    }
//
//    suspend fun insertTrendingRepositories(trendingRepositoriesEntity: TrendingRepositoriesEntity) {
//        trendingRepositoriesItems.add(trendingRepositoriesEntity)
//        refreshLifeData()
//    }

}