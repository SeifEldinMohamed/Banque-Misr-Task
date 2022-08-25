package com.seif.banquemisrttask.di

import android.content.Context
import android.net.ConnectivityManager
import com.seif.banquemisrttask.domain.repository.Repository
import com.seif.banquemisrttask.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Singleton
    @Provides
    fun provideGetTrendingRepositoriesUseCase(
        repository: Repository
    ): GetTrendingRepositoriesUseCase {
        return GetTrendingRepositoriesUseCase(repository)
    }
    @Singleton
    @Provides
    fun checkInternetConnection (context: Context): ConnectivityManager {
       return  context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
    }

}