package com.seif.banquemisrttask.data.datasources.localdatasource.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.seif.banquemisrttask.data.datasources.remotedatasource.models.TrendingRepositoriesItem
import com.seif.banquemisrttask.data.datasources.remotedatasource.models.TrendingRepositoriesResponse
import com.seif.banquemisrttask.util.Constants.Companion.TRENDING_REPOSITORIES_TABLE
import kotlinx.parcelize.Parcelize

@Entity(tableName = TRENDING_REPOSITORIES_TABLE)
@Parcelize
data class TrendingRepositoriesEntity(
    @PrimaryKey(autoGenerate = false)
    val id:Int,
    val author: String?,
    val avatar: String?,
    val description: String?,
    val forks: Int,
    val language: String?,
    val languageColor: String?,
    val name: String,
    val stars: Int,
    val url: String,
    val fetchTimeStamp: Long
): Parcelable