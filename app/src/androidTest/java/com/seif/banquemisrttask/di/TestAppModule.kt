package com.seif.banquemisrttask.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.seif.banquemisrttask.data.datasources.localdatasource.RepositoriesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("test_db")
    fun provideInMemoryDb( @ApplicationContext context: Context) : RepositoriesDatabase {
       return Room.inMemoryDatabaseBuilder(context, RepositoriesDatabase::class.java)
            .allowMainThreadQueries() // Disables the main thread query check for Room.
            .build()
    }

}