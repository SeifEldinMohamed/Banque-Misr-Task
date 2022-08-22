package com.seif.banquemisrttask.data.datasources.localdatasource

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seif.banquemisrttask.data.datasources.remotedatasource.models.TrendingRepositories

class RepositoriesTypeConverter {

    @TypeConverter
    fun trendingRepositoriesToString(trendingRepositories: TrendingRepositories):String {
        return Gson().toJson(trendingRepositories)
    }
    @TypeConverter
    fun fromStringToTrendingRepositories(data: String):TrendingRepositories {
        val listType = object : TypeToken<TrendingRepositories>(){}.type
        return Gson().fromJson(data,listType)
    }
}