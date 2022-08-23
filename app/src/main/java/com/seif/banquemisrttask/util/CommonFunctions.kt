package com.seif.banquemisrttask.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class CommonFunctions {

//        fun checkInternetConnection (): Boolean{
//            val connectivityManager = context.getSystemService(
//                Context.CONNECTIVITY_SERVICE
//            ) as ConnectivityManager
//
//            val activeNetwork: Network = connectivityManager.activeNetwork ?: return false
//            val capabilities: NetworkCapabilities =
//                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
//
//            return when { // return true if there is an internet connection from wifi, cellular and ethernet
//                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
//                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
//                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
//                else -> false
//            }
//        }

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    fun checkInternetConnection():Boolean {
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