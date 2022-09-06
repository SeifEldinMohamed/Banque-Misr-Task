package com.seif.banquemisrttask.data.datasources.localdatasource

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seif.banquemisrttask.data.datasources.localdatasource.entities.TrendingRepositoriesEntity

class RepositoriesTypeConverter {

    @TypeConverter
    fun trendingRepositoriesToString(trendingRepositoriesResponse: List<TrendingRepositoriesEntity>):String {
        return Gson().toJson(trendingRepositoriesResponse)
    }
    @TypeConverter
    fun fromStringToTrendingRepositories(data: String):List<TrendingRepositoriesEntity> {
        val listType = object : TypeToken<List<TrendingRepositoriesEntity>>(){}.type
        return Gson().fromJson(data,listType)
    }
}