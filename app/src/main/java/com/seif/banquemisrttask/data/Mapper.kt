package com.seif.banquemisrttask.data

import androidx.compose.runtime.mutableStateOf
import com.seif.banquemisrttask.data.datasources.localdatasource.entities.TrendingRepositoriesEntity
import com.seif.banquemisrttask.data.datasources.remotedatasource.dto.TrendingRepositoriesItem
import com.seif.banquemisrttask.domain.model.TrendingRepository

fun List<TrendingRepositoriesItem>.toTrendingRepositoriesEntityList(): List<TrendingRepositoriesEntity> {
    val list = ArrayList<TrendingRepositoriesEntity>()
    this.forEachIndexed { index, element->
        list.add(
            TrendingRepositoriesEntity(
                index,
                element.author,
                element.avatar,
                element.description,
                element.forks,
                element.language,
                element.languageColor,
                element.name,
                element.stars,
                element.url,
                System.currentTimeMillis(),
                false
            )
        )
    }
    return list
}

fun List<TrendingRepositoriesEntity>.toTrendingRepository(): List<TrendingRepository> {
    return this.map {
        TrendingRepository(
            it.id,
            it.author ?: "Seif Mohamed",
            it.avatar?: "https://picsum.photos/seed/picsum/200/300",
            it.description?: "",
            it.id,
            it.language?: "Kotlin",
            it.languageColor?: "#0000FF",
            it.name,
            it.id,
            it.url,
            it.fetchTimeStamp,
            mutableStateOf(false)
        )
    }
}