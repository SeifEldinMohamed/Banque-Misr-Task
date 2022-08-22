package com.seif.banquemisrttask.data.datasources.localdatasource.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.seif.banquemisrttask.data.datasources.remotedatasource.models.TrendingRepositories
import com.seif.banquemisrttask.util.Constants.Companion.TRENDING_REPOSITORIES_TABLE

@Entity(tableName = TRENDING_REPOSITORIES_TABLE)
data class TrendingRepositoriesEntity(
    @PrimaryKey(autoGenerate = false) // to keep only the newest data coming from api (only one row)
    var id: Int,
    var trendingRepositories: TrendingRepositories
)