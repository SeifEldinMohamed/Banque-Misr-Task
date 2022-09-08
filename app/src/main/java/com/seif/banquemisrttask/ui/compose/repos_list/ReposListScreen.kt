package com.seif.banquemisrttask.ui.compose.repos_list

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.seif.banquemisrttask.ui.compose.HomeViewModel
import com.seif.banquemisrttask.ui.compose.rememberWindowInfo
import com.seif.banquemisrttask.ui.compose.WindowInfo
import com.seif.banquemisrttask.ui.compose.repos_list.components.*

@Composable
fun ReposListScreen() {
    Column {
        OptionMenu()
        SwipeRefreshCompose()

    }
}

@Composable
fun SwipeRefreshCompose(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    var refreshing by remember { mutableStateOf(false) }
    LaunchedEffect(refreshing) {
        if (refreshing) { // do your work here
            homeViewModel.forceFetchingData()
            //delay(1500)
            refreshing = false
        }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = refreshing),
        onRefresh = { refreshing = true },
    ) {
        val stateValue = homeViewModel.state.value
        val windowInfo = rememberWindowInfo()
        if (stateValue.error.isNotBlank()) {
            when(windowInfo.screenWidthInfo){
                is WindowInfo.WindowType.Compact -> NoInternetConnectionSectionPortrait()
                else -> NoInternetConnectionSectionLandscape()
            }

        }
        if (stateValue.isLoading) {
            Log.d("trending", "loading...")
            Column {
                repeat(10) {
                    AnimatedShimmer()
                }
            }
        }
        if (stateValue.repos.isNotEmpty()) {
            val reposListStateValue by rememberSaveable { // to save state when configuration changed
                mutableStateOf(stateValue.repos)
            }
            RepositoriesLazyColumn(reposList = reposListStateValue)
        }
        if (stateValue.sortedReposByName.isNotEmpty()){
            val sortedListByNameStateValue by rememberSaveable { // to save state when configuration changed
                mutableStateOf(stateValue.sortedReposByName)
            }
            RepositoriesLazyColumn(reposList = sortedListByNameStateValue)
        }
        if (stateValue.sortedReposByStars.isNotEmpty()){
            val sortedListByStarsStateValue by rememberSaveable { // to save state when configuration changed
                mutableStateOf(stateValue.sortedReposByStars)
            }
            RepositoriesLazyColumn(reposList = sortedListByStarsStateValue)
        }
    }
}





