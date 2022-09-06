package com.seif.banquemisrttask.ui.compose.repos_list.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.seif.banquemisrttask.domain.model.TrendingRepository
import com.seif.banquemisrttask.ui.compose.HomeViewModel

@Composable
fun RepositoriesLazyColumn(
    reposList: List<TrendingRepository>,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    androidx.compose.foundation.lazy.LazyColumn(modifier = Modifier.padding(horizontal = 10.dp)) {
        itemsIndexed(reposList) { index, item ->
            RepoItemExpandable(
                repositories = item,
                index,
                onItemClick = {
                    if (homeViewModel.previousSelectedPosition == -1) {
                        homeViewModel.previousSelectedPosition = it
                        reposList[it].isExpanded.value = true
                    } else if (it == homeViewModel.previousSelectedPosition) {
                        reposList[it].isExpanded.value = !reposList[it].isExpanded.value
                    } else { // not equal
                        reposList[it].isExpanded.value = true
                        reposList[homeViewModel.previousSelectedPosition].isExpanded.value = false
                        homeViewModel.previousSelectedPosition = it
                    }
                })
        }
    }
}