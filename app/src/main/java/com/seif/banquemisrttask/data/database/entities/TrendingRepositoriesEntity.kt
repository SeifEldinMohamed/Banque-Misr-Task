package com.seif.banquemisrttask.data.database.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.seif.banquemisrttask.data.network.models.TrendingRepositories
import com.seif.banquemisrttask.util.Constants.Companion.TRENDING_REPOSITORIES_TABLE
import kotlinx.parcelize.Parcelize

@Entity(tableName = TRENDING_REPOSITORIES_TABLE)
data class TrendingRepositoriesEntity(
    @PrimaryKey(autoGenerate = false) // to keep only the newest data coming from api (only one row)
    var id: Int,
    var trendingRepositories: TrendingRepositories
)