package com.seif.banquemisrttask.di

import android.content.Context
import com.seif.banquemisrttask.domain.repository.LocalRepository
import com.seif.banquemisrttask.domain.repository.RemoteRepository
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
        localRepository: LocalRepository,
        remoteRepository: RemoteRepository
    ): GetTrendingRepositoriesUseCase {
        return GetTrendingRepositoriesUseCase(localRepository, remoteRepository)
    }

    @Singleton
    @Provides
    fun provideShouldFetchDataUseCase(remoteRepository: RemoteRepository): ShouldFetchDataUseCase {
        return ShouldFetchDataUseCase(remoteRepository)
    }

    @Singleton
    @Provides
    fun provideReadTrendingRepositoriesUseCase(localRepository: LocalRepository): ReadTrendingRepositoriesUseCase {
        return ReadTrendingRepositoriesUseCase(localRepository)
    }

    @Singleton
    @Provides
    fun provideSortTrendingRepositoriesUseCase(): SortTrendingRepositoriesUseCase {
        return SortTrendingRepositoriesUseCase()
    }

    @Singleton
    @Provides
    fun provideCheckInternetConnectionUseCase(context: Context): CheckInternetConnectionUseCase {
        return CheckInternetConnectionUseCase(context)
    }
}