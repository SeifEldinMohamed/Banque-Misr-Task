package com.seif.banquemisrttask.util

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import javax.inject.Inject

class CommonFunctions {
    companion object {
        fun checkInternetConnection(connectivityManager: ConnectivityManager): Boolean {
            val activeNetwork: Network = connectivityManager.activeNetwork ?: return false
            val capabilities: NetworkCapabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            Log.d("trending","test check network")
            return when { // return true if there is an internet connection from wifi, cellular and ethernet
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
    }
}