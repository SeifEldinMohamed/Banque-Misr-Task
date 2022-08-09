package com.seif.banquemisrttask.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.seif.banquemisrttask.data.network.models.TrendingRepositories
import com.seif.banquemisrttask.util.Constants.Companion.TRENDING_REPOSITORIES_TABLE

@Entity(tableName = TRENDING_REPOSITORIES_TABLE)
class TrendingRepositoriesEntity(
    var trendingRepositories: TrendingRepositories
) {
    @PrimaryKey(autoGenerate = false) // to keep only the newest data coming from api (only one row)
    val id: Int = 0
}