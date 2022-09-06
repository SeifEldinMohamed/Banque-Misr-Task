package com.seif.banquemisrttask.ui.compose

import com.seif.banquemisrttask.data.datasources.localdatasource.entities.TrendingRepositoriesEntity
import com.seif.banquemisrttask.domain.model.TrendingRepository

data class UiState(
    val isLoading: Boolean = false,
    val repos: List<TrendingRepository> = emptyList(),
    val sortedReposByName: List<TrendingRepositoriesEntity> = emptyList(),
    val sortedReposByStars: List<TrendingRepositoriesEntity> = emptyList(),
    val error: String = "",
    val isSwipingToRefresh: Boolean = false,
    val isRetry: Boolean = false
)