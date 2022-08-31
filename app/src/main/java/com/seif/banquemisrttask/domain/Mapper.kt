package com.seif.banquemisrttask.domain

import com.seif.banquemisrttask.data.datasources.localdatasource.entities.TrendingRepositoriesEntity
import com.seif.banquemisrttask.data.datasources.remotedatasource.models.TrendingRepositoriesItem

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