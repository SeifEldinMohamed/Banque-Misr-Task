package com.seif.banquemisrttask.util

class Constants {
    companion object {
        // retrofit
        const val BASE_URL = "https://api.github.com/"
        // encapsulate base url in c++ file
        // ROOM
        const val DATABASE_NAME = "repositories_database"
        const val TRENDING_REPOSITORIES_TABLE = "trending_table"

        // two hours in milliseconds
        const val TWO_HOURS_INTERVAL = 7200000L
    }
}
// https://api.github.com/repositories
// https://ghapi.huchen.dev/repositories