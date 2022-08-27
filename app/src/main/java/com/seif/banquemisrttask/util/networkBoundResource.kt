package com.seif.banquemisrttask.util

import android.util.Log
import kotlinx.coroutines.flow.*

// we used inline keyword to make this function more efficient ( as it doesn't necessarly create object for these functions when it compiles the code instead it takes the contents of these functions and copy them into the place where they called
inline fun <ResultType, RequestType> networkBoundResource( // we use this type arguments to keep this function reusable with different kind of data and we want 2 of them because it's not always is the type of the data you get from api the same as the type you want to store in database
    crossinline query: () -> Flow<ResultType>, // responsible for getting the data from the database (will return a flow of ResultType)
    crossinline fetch: suspend () -> RequestType, // responsible for fetching new data from api (return list of trending repos)
    crossinline saveFetchResult: suspend (RequestType) -> Unit, // responsible for taking data coming from api ( fetch() ) and then caching it
    crossinline shouldFetch: (ResultType) -> Boolean = { true }, // responsible for saying if we should fetch data or not (default value = true: this means that if we didn't pass something to shouldFetch then it will return true
    crossinline hasInternetConnection: () -> Boolean = { true }  // responsible for checking internet connection
) = flow {

    val data = query().first() // to get first value in flow (one list of trending list)
    val flow = if (shouldFetch(data)) {
        emit(NetworkResult.Loading()) // loading state
        if (hasInternetConnection()) {
            try {
                saveFetchResult(fetch()) // will take the returning type of fetch then save it in database
                query().map { NetworkResult.Success(it) } // to convert Flow<List<TrendingRepositoriesEntity>> to Flow<NetworkResult<List<TrendingRepositoriesEntity>>>
            } catch (e: Exception) { // for example getting error from server
                query().map {
                    NetworkResult.Error(
                        e.message,
                        it
                    )
                } // get old data that cached in database when error happened
            }
        } else { // no internet connection
            query().map { NetworkResult.Error("No Internet Connection", it) }
        }
    } else { // get cached data from database
        query().map { NetworkResult.Success(it) } //  // to convert Flow<List<TrendingRepositoriesEntity>> to Flow<NetworkResult<List<TrendingRepositoriesEntity>>>
    }

    emitAll(flow) // we used emitAll because we want to emit all the values not a single one
} // this block will be executed whenever we called this networkBoundResourceFunction

// we are not allowed to call return in any of these functions arguments (query(), fetch(), ...) when we inside this flow block
// crossinline modifier: so this modifier makes sure that we aren't allowed to call return in the function arguments that we later pass to the networkBoundResource()