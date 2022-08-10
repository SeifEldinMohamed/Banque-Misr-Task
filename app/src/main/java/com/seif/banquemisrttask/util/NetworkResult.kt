package com.seif.banquemisrttask.util

sealed class NetworkResult<T>( // we will use this class in viewModel to parse our response from api
    val data: T? = null, // actual data from api
    val message: String? = null // message
) {
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String?, data: T? = null) : NetworkResult<T>(data, message) // if error happened than data = null

    class Loading<T> : NetworkResult<T>()
}