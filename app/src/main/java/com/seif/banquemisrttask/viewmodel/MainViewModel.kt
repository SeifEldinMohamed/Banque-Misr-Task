package com.seif.banquemisrttask.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.*
import com.seif.banquemisrttask.data.database.entities.TrendingRepositoriesEntity
import com.seif.banquemisrttask.data.network.models.TrendingRepositories
import com.seif.banquemisrttask.data.network.models.TrendingRepositoriesItem
import com.seif.banquemisrttask.data.repositories.Repository
import com.seif.banquemisrttask.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) { // since we will need an application reference, so we will use AndroidViewModel

    /** ROOM Database **/
    val readTrendingRepositories: LiveData<List<TrendingRepositoriesEntity>> by lazy {
        repository.readTrendingRepositories().asLiveData()
    }

    /** Retrofit **/
    val trendingRepositoriesResponse: MutableLiveData<NetworkResult<TrendingRepositories>> by lazy {
        MutableLiveData()
    }

    fun shouldFetchData(): Boolean {
        return repository.shouldFetchData()
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
                repository.getTrendingRepositories()?.let {
                    trendingRepositoriesResponse.postValue(it)
                }
            } catch (e: Exception) {
                trendingRepositoriesResponse.postValue(NetworkResult.Error("something went wrong ${e.message}"))
            }
        }
        else {
            trendingRepositoriesResponse.postValue(NetworkResult.Error("No Internet Connection"))
        }
    }

    // function to check internet connectivity ( returns true when internet is reliable and it will return false if not
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activeNetwork: Network = connectivityManager.activeNetwork ?: return false
        val capabilities: NetworkCapabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when { // return true if there is an internet connection from wifi, cellular and ethernet
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    suspend fun sortReposByName(trendingRepositoriesEntity: ArrayList<TrendingRepositoriesEntity>): ArrayList<TrendingRepositoriesItem> {
        return withContext(Dispatchers.IO) {
            trendingRepositoriesEntity[0].trendingRepositories.sortedBy { item ->
                item.name
            }.toCollection(ArrayList())
        }
    }

    suspend fun sortReposByStars(trendingRepositoriesEntity: ArrayList<TrendingRepositoriesEntity>): ArrayList<TrendingRepositoriesItem> {
        return withContext(Dispatchers.IO) {
            trendingRepositoriesEntity[0].trendingRepositories.sortedBy { item ->
                item.stars
            }.toCollection(ArrayList())
        }
    }

}

// A cellular network or mobile network is a type of wireless connection facilitated by cellular towers.
// To have access to the cellular network, your mobile devices will have to be connected through a cellular provider

// ethernet is used to connect devices within a local area network (LAN). It’s a much smaller connection system than the internet.

// WiFi is capable of doing most of this wirelessly, which is more convenient, but slower.

// 2 hours = 7200000 milliseconds