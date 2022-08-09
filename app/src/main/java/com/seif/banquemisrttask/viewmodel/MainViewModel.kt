package com.seif.banquemisrttask.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.seif.banquemisrttask.data.Repository
import com.seif.banquemisrttask.data.network.models.TrendingRepositories
import com.seif.banquemisrttask.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) { // since we will need a application reference so we will use AndroidViewModel

    var trendingRepositoriesResponse: MutableLiveData<NetworkResult<TrendingRepositories>> =
        MutableLiveData()

    fun getTrendingRepositories() {
        viewModelScope.launch {
            getTrendingRepositoriesSafeCall()
        }
    }

    private suspend fun getTrendingRepositoriesSafeCall() {
        trendingRepositoriesResponse.value = NetworkResult.Loading() // loading state until we get data from api
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getTrendingRepositories()
                trendingRepositoriesResponse.value = handleTrendingRepositoriesResponse(response)
            }
            catch (e:Exception) {
                trendingRepositoriesResponse.value = NetworkResult.Error("something went wrong ${e.message}")
            }

        } else {
            trendingRepositoriesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private fun handleTrendingRepositoriesResponse(response: Response<TrendingRepositories>): NetworkResult<TrendingRepositories>? {
        return when {
            response.message().toString().contains("timeout") -> NetworkResult.Error("Timeout")
            response.code() == 404 -> NetworkResult.Error("Not Found")
            response.body().isNullOrEmpty() -> NetworkResult.Error("Trending Repositories Not Found.")
            response.isSuccessful -> { // we will return trending repositories from api
                response.body()?.let {
                    NetworkResult.Success(it)
                }
            }
            else -> NetworkResult.Error(response.message())
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
}

// A cellular network or mobile network is a type of wireless connection facilitated by cellular towers.
// To have access to the cellular network, your mobile devices will have to be connected through a cellular provider

// ethernet is used to connect devices within a local area network (LAN). It’s a much smaller connection system than the internet.

// WiFi is capable of doing most of this wirelessly, which is more convenient, but slower.