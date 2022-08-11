package com.seif.banquemisrttask.di

import android.content.Context
import androidx.room.Room
import com.seif.banquemisrttask.data.Repository
import com.seif.banquemisrttask.data.database.LocalDataSource
import com.seif.banquemisrttask.data.database.RepositoriesDatabase
import com.seif.banquemisrttask.data.database.TrendingRepositoriesDao
import com.seif.banquemisrttask.data.network.RemoteDataSource
import com.seif.banquemisrttask.data.network.TrendingRepositoriesApi
import com.seif.banquemisrttask.util.Constants.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        RepositoriesDatabase::class.java,
        DATABASE_NAME
    ).fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideDao(database: RepositoriesDatabase) = database.trendingRepositoriesDao()

    @Singleton
    @Provides
    fun provideDefaultShoppingRepository( // since we pass a ShoppingRepository Interface in our ViewModel Constructor dagger will look if we provide such an interface
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource
    ) = Repository(localDataSource, remoteDataSource) as com.seif.banquemisrttask.data.repositories.Repository
}

// If you don’t want to provide migrations and you specifically want
// your database to be cleared when you upgrade the version,
// call fallbackToDestructiveMigration in the database builder