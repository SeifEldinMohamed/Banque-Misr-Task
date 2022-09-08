package com.seif.banquemisrttask.ui.compose.repos_list.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
    LazyColumn(modifier = Modifier.padding(horizontal = 10.dp)) {
        itemsIndexed(items = reposList, key = {index, item -> item.id }) { index, item ->
            RepoItemExpandable(
                repositories = item,
                index,
                onItemClick = {
                    homeViewModel.handleExpandedState(reposList, it)
                }
            )
        }
    }
}