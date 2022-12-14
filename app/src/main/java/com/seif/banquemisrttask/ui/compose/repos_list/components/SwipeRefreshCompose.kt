package com.seif.banquemisrttask.ui.compose.repos_list.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.seif.banquemisrttask.ui.compose.HomeViewModel
import com.seif.banquemisrttask.ui.compose.rememberWindowInfo
import com.seif.banquemisrttask.ui.compose.WindowInfo

@Composable
fun SwipeRefreshCompose(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    var refreshing by remember { mutableStateOf(false) }
    LaunchedEffect(refreshing) {
        if (refreshing) { // do your work here
            homeViewModel.forceFetchingData()
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
            val reposListStateValue by remember { // to save state when configuration changed
                mutableStateOf(stateValue.repos)
            }
            RepositoriesLazyColumn(reposList = reposListStateValue)
        }
        if (stateValue.sortedReposByName.isNotEmpty()){
            val sortedListByNameStateValue by remember { // to save state when configuration changed
                mutableStateOf(stateValue.sortedReposByName)
            }
            RepositoriesLazyColumn(reposList = sortedListByNameStateValue)
        }
        if (stateValue.sortedReposByStars.isNotEmpty()){
            val sortedListByStarsStateValue by remember { // to save state when configuration changed
                mutableStateOf(stateValue.sortedReposByStars)
            }
            RepositoriesLazyColumn(reposList = sortedListByStarsStateValue)
        }
    }
}