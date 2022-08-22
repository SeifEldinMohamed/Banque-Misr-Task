package com.seif.banquemisrttask.ui.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.*
import com.seif.banquemisrttask.data.datasources.localdatasource.entities.TrendingRepositoriesEntity
import com.seif.banquemisrttask.data.datasources.remotedatasource.models.TrendingRepositories
import com.seif.banquemisrttask.data.datasources.remotedatasource.models.TrendingRepositoriesItem
import com.seif.banquemisrttask.domain.usecases.*
import com.seif.banquemisrttask.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getTrendingRepositoriesUseCase: GetTrendingRepositoriesUseCase,
    private val shouldFetchDataUseCase: ShouldFetchDataUseCase,
    private val readTrendingRepositoriesUseCase: ReadTrendingRepositoriesUseCase,
    private val sortTrendingRepositoriesUseCase: SortTrendingRepositoriesUseCase,
    private val checkInternetConnectionUseCase: CheckInternetConnectionUseCase,
    application: Application
) : AndroidViewModel(application) { // since we will need an application reference, so we will use AndroidViewModel

    /** ROOM Database **/
    val readTrendingRepositories: LiveData<List<TrendingRepositoriesEntity>> by lazy {
        readTrendingRepositoriesUseCase().asLiveData()
    }

    /** Retrofit **/
    val trendingRepositoriesResponse: MutableLiveData<NetworkResult<TrendingRepositories>> by lazy {
        MutableLiveData()
    }

    fun shouldFetchData(): Boolean {
        return shouldFetchDataUseCase()
    }

    fun getTrendingRepositories() {
        viewModelScope.launch(Dispatchers.IO) {
            getTrendingRepositoriesSafeCall()
        }
    }

    private suspend fun getTrendingRepositoriesSafeCall() {
        trendingRepositoriesResponse.postValue(NetworkResult.Loading()) // loading state until we get data from api
        if (hasInternetConnection()) {
            Log.d("trending", "request data form api")
            try {
                getTrendingRepositoriesUseCase()?.let {
                    trendingRepositoriesResponse.postValue(it)
                }
            } catch (e: Exception) {
                trendingRepositoriesResponse.postValue(NetworkResult.Error("something went wrong ${e.message}"))
            }
        } else {
            trendingRepositoriesResponse.postValue(NetworkResult.Error("No Internet Connection"))
        }
    }

    // function to check internet connectivity ( returns true when internet is reliable and it will return false if not
    private fun hasInternetConnection(): Boolean {
        return checkInternetConnectionUseCase()
    }

     suspend fun sortReposByName(trendingRepositoriesEntity: ArrayList<TrendingRepositoriesEntity>): ArrayList<TrendingRepositoriesItem> {
        return sortTrendingRepositoriesUseCase.sortTrendingRepositoriesByName(
            trendingRepositoriesEntity
        )
    }

     suspend fun sortReposByStars(trendingRepositoriesEntity: ArrayList<TrendingRepositoriesEntity>): ArrayList<TrendingRepositoriesItem> {
        return sortTrendingRepositoriesUseCase.sortTrendingRepositoriesByStars(
            trendingRepositoriesEntity
        )
    }

}

// A cellular network or mobile network is a type of wireless connection facilitated by cellular towers.
// To have access to the cellular network, your mobile devices will have to be connected through a cellular provider

// ethernet is used to connect devices within a local area network (LAN). It’s a much smaller connection system than the internet.

// WiFi is capable of doing most of this wirelessly, which is more convenient, but slower.

// 2 hours = 7200000 milliseconds