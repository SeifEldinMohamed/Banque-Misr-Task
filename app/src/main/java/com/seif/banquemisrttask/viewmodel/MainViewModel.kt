package com.seif.banquemisrttask.viewmodel

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.*
import com.seif.banquemisrttask.data.Repository
import com.seif.banquemisrttask.data.database.entities.TrendingRepositoriesEntity
import com.seif.banquemisrttask.data.network.models.TrendingRepositories
import com.seif.banquemisrttask.data.network.models.TrendingRepositoriesItem
import com.seif.banquemisrttask.data.database.sharedprefrence.AppSharedPreference
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
    val readTrendingRepositories: LiveData<List<TrendingRepositoriesEntity>> by lazy {
        repository.locale.readTrendingRepositories().asLiveData()
    }

    private fun insertTrendingRepositories(trendingRepositoriesEntity: TrendingRepositoriesEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.locale.insertTrendingRepositories(trendingRepositoriesEntity)
        }
    }

    private fun offlineCacheRepositories(trendingRepositories: TrendingRepositories) {
        val trendingRepositoriesEntity = TrendingRepositoriesEntity(0, trendingRepositories)
        insertTrendingRepositories(trendingRepositoriesEntity)
    }

    /** Retrofit **/

    val trendingRepositoriesResponse: MutableLiveData<NetworkResult<TrendingRepositories>> by lazy {
        MutableLiveData()
    }

    // call data from api if it's first time user enter app
    fun requestDataForFirstTime(context: Context) {
        AppSharedPreference.init(context)
        AppSharedPreference.readIsFirstTime("isFirstTime", true)?.let {
            if (it) {
                getTrendingRepositories()
                scheduleAlarmToRefreshCachedData(context)
                AppSharedPreference.writeIsFirstTime("isFirstTime", false)
            }
        }
    }

    fun getTrendingRepositories() {
        viewModelScope.launch {
            getTrendingRepositoriesSafeCall()
        }
    }

    private suspend fun getTrendingRepositoriesSafeCall() {
        // loading state until we get data from api
        trendingRepositoriesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            Log.d("viewModel", "request data form api")
            try {
                val response: Response<TrendingRepositories> =
                    repository.remote.getTrendingRepositories()

                // success or failure
                trendingRepositoriesResponse.value = handleTrendingRepositoriesResponse(response)

            } catch (e: Exception) {
                trendingRepositoriesResponse.value =
                    NetworkResult.Error("something went wrong ${e.message}")
            }
        } else {
            trendingRepositoriesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private fun handleTrendingRepositoriesResponse(response: Response<TrendingRepositories>): NetworkResult<TrendingRepositories>? {
        return when {
            response.message().toString().contains("timeout") -> NetworkResult.Error("Timeout")
            response.code() == 404 -> NetworkResult.Error("Not Found")
            response.body()
                .isNullOrEmpty() -> NetworkResult.Error("Trending Repositories Not Found.")

            response.isSuccessful -> { // we will return trending repositories from api
                response.body()?.let {
                    // caching data
                    offlineCacheRepositories(it)
                    Log.d("trending", "data cached in database")

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

    fun sortReposByName(trendingRepositoriesEntity: ArrayList<TrendingRepositoriesEntity>): ArrayList<TrendingRepositoriesItem> {
        return trendingRepositoriesEntity[0].trendingRepositories.sortedBy { item ->
            item.name
        }.toCollection(ArrayList())
    }

    fun sortReposByStars(trendingRepositoriesEntity: ArrayList<TrendingRepositoriesEntity>): ArrayList<TrendingRepositoriesItem> {
        return trendingRepositoriesEntity[0].trendingRepositories.sortedBy { item ->
            item.stars
        }.toCollection(ArrayList())
    }

    private fun scheduleAlarmToRefreshCachedData(context: Context) { // refresh cached data every 2 hours
        val alarmIntent = Intent(context, TrendingActivity.AlarmBroadcastReceiver()::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager : AlarmManager =  context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, // type of Alarm
            System.currentTimeMillis()   , // time in milliseconds that the alarm should first go off, using the appropriate clock (depending on the alarm type).
            TWO_HOURS_INTERVAL, // interval in milliseconds between subsequent repeats of the alarm.
            pendingIntent // Action to perform when the alarm goes off
        )
        Log.d("trending", "scheduled alarm manager to goes of at ${System.currentTimeMillis() + TWO_HOURS_INTERVAL}")
    }

}

// A cellular network or mobile network is a type of wireless connection facilitated by cellular towers.
// To have access to the cellular network, your mobile devices will have to be connected through a cellular provider

// ethernet is used to connect devices within a local area network (LAN). It’s a much smaller connection system than the internet.

// WiFi is capable of doing most of this wirelessly, which is more convenient, but slower.

// 2 hours = 7200000 milliseconds