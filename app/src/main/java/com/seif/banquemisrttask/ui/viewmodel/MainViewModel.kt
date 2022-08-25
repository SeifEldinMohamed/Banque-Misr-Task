package com.seif.banquemisrttask.ui.viewmodel

import androidx.lifecycle.*
import com.seif.banquemisrttask.data.datasources.localdatasource.entities.TrendingRepositoriesEntity
import com.seif.banquemisrttask.domain.repository.Repository
import com.seif.banquemisrttask.domain.usecases.*
import com.seif.banquemisrttask.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getTrendingRepositoriesUseCase: GetTrendingRepositoriesUseCase,
    private val repository: Repository
) : ViewModel() { // since we will need an application reference, so we will use AndroidViewModel

    /** Retrofit **/
    var trendingRepositoriesResponse: LiveData<NetworkResult<List<TrendingRepositoriesEntity>>> =
        getTrendingRepositoriesUseCase(false).asLiveData()

    /** Sorting **/
    val sortReposByName: LiveData<List<TrendingRepositoriesEntity>> by lazy {
        repository.sortTrendingRepositoriesByName().asLiveData()
    }
    val sortReposByStars: LiveData<List<TrendingRepositoriesEntity>> by lazy {
        repository.sortTrendingRepositoriesByStars().asLiveData()
    }


    fun forceFetchingData() {
        trendingRepositoriesResponse = getTrendingRepositoriesUseCase(true).asLiveData()
    }


//    fun getTrendingRepositories() {
//        viewModelScope.launch(Dispatchers.IO) {
//            getTrendingRepositoriesSafeCall()
//        }
//    }
//
//    private fun getTrendingRepositoriesSafeCall() {
//        trendingRepositoriesResponse.postValue(NetworkResult.Loading())  // loading state until we get data from api
//        Log.d("trending", "request data form api")
//        try {
//            trendingRepositoriesResponse.postValue(trendingRepositoriesResponse.value)
//
//        } catch (e: Exception) {
//            trendingRepositoriesResponse.postValue(NetworkResult.Error("something went wrong ${e.message}"))
//        }
//
//    }


}

// A cellular network or mobile network is a type of wireless connection facilitated by cellular towers.
// To have access to the cellular network, your mobile devices will have to be connected through a cellular provider

// ethernet is used to connect devices within a local area network (LAN). It’s a much smaller connection system than the internet.

// WiFi is capable of doing most of this wirelessly, which is more convenient, but slower.

// 2 hours = 7200000 milliseconds