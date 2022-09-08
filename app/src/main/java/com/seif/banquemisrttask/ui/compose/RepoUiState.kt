package com.seif.banquemisrttask.ui.compose

import com.seif.banquemisrttask.domain.model.TrendingRepository

data class RepoUiState(
    val isLoading: Boolean = false,
    val repos: List<TrendingRepository> = emptyList(),
    val sortedReposByName: List<TrendingRepository> = emptyList(),
    val sortedReposByStars: List<TrendingRepository> = emptyList(),
    val error: String = "",
    val isSwipingToRefresh: Boolean = false,
    val isRetry: Boolean = false
)