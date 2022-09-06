package com.seif.banquemisrttask.domain.model

import androidx.compose.runtime.MutableState

data class TrendingRepository(
    val id:Int,
    val author: String,
    val avatar: String,
    val description: String,
    val forks: Int,
    val language: String,
    val languageColor: String,
    val name: String,
    val stars: Int,
    val url: String,
    val fetchTimeStamp: Long,
    val isExpanded: MutableState<Boolean>
)
