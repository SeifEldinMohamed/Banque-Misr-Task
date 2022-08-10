package com.seif.banquemisrttask.viewmodel

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.seif.banquemisrttask.data.Repository
import com.seif.banquemisrttask.data.database.entities.TrendingRepositoriesEntity
import com.seif.banquemisrttask.data.network.models.TrendingRepositories
import com.seif.banquemisrttask.data.network.models.TrendingRepositoriesItem
import com.seif.banquemisrttask.ui.TrendingActivity
import com.seif.banquemisrttask.util.Constants.Companion.TWO_HOURS_INTERVAL
import com.seif.banquemisrttask.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) { // since we will need a application reference so we will use AndroidViewModel

    /** ROOM Database **/
    val readTrendingRepositories: LiveData<List<TrendingRepositoriesEntity>> = repository.locale.readTrendingRepositories().asLiveData()

    private fun insertTrendingRepositories(trendingRepositoriesEntity: TrendingRepositoriesEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.locale.insertTrendingRepositories(trendingRepositoriesEntity)
        }
    }

    private fun offlineCacheRepositories(trendingRepositories: TrendingRepositories) {
        val trendingRepositoriesEntity = TrendingRepositoriesEntity(trendingRepositories)
        insertTrendingRepositories(trendingRepositoriesEntity)
    }

    /** Retrofit **/

    var trendingRepositoriesResponse: MutableLiveData<NetworkResult<TrendingRepositories>> =
        MutableLiveData()

    fun getTrendingRepositories() {
        viewModelScope.launch {
            getTrendingRepositoriesSafeCall()
        }
    }

    private suspend fun getTrendingRepositoriesSafeCall() {
        // loading state until we get data from api
        trendingRepositoriesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response: Response<TrendingRepositories> = repository.remote.getTrendingRepositories()
                // success or failure
                trendingRepositoriesResponse.value = handleTrendingRepositoriesResponse(response)

                // caching data
                val trendingRepositories: TrendingRepositories? = trendingRepositoriesResponse.value!!.data
                trendingRepositories?.let {
                    offlineCacheRepositories(it)
                }
            } catch (e: Exception) {
                trendingRepositoriesResponse.value = NetworkResult.Error("something went wrong ${e.message}")
            }

        }
        else {
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

    fun sortReposByName(trendingRepositoriesEntity: List<TrendingRepositoriesEntity>): List<TrendingRepositoriesItem> {
        return trendingRepositoriesEntity[0].trendingRepositories.sortedBy { trendingRepositoriesItem ->
            trendingRepositoriesItem.name
        }
    }


}

// A cellular network or mobile network is a type of wireless connection facilitated by cellular towers.
// To have access to the cellular network, your mobile devices will have to be connected through a cellular provider

// ethernet is used to connect devices within a local area network (LAN). It’s a much smaller connection system than the internet.

// WiFi is capable of doing most of this wirelessly, which is more convenient, but slower.

// 2 hours = 7200000 milliseconds