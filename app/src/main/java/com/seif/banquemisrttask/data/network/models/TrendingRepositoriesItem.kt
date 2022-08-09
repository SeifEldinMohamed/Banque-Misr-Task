package com.seif.banquemisrttask.data.network.models


import com.google.gson.annotations.SerializedName

data class TrendingRepositoriesItem(
    @SerializedName("author")
    val author: String,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("forks")
    val forks: Int,
    @SerializedName("language")
    val language: String,
    @SerializedName("languageColor")
    val languageColor: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("stars")
    val stars: Int,
    @SerializedName("url")
    val url: String
)