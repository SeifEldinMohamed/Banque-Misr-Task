package com.seif.banquemisrttask.data.network.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrendingRepositoriesItem(
    val author: String,
    val avatar: String,
    val description: String,
    val forks: Int,
    val language: String,
    val languageColor: String,
    val name: String,
    val stars: Int,
    val url: String
): Parcelable