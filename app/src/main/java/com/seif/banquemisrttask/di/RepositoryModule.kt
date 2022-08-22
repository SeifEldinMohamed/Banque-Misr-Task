package com.seif.banquemisrttask.di

import com.seif.banquemisrttask.data.datasources.localdatasource.LocalDataSource
import com.seif.banquemisrttask.data.datasources.remotedatasource.RemoteDataSource
import com.seif.banquemisrttask.data.repositoryImp.LocalRepositoryImp
import com.seif.banquemisrttask.data.repositoryImp.RemoteRepositoryImp
import com.seif.banquemisrttask.domain.repository.LocalRepository
import com.seif.banquemisrttask.domain.repository.RemoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideRemoteRepository( // since we pass a ShoppingRepository Interface in our ViewModel Constructor dagger will look if we provide such an interface
        remoteDataSource: RemoteDataSource
    ) = RemoteRepositoryImp(remoteDataSource) as RemoteRepository

    @Singleton
    @Provides
    fun provideLocalRepository( // since we pass a ShoppingRepository Interface in our ViewModel Constructor dagger will look if we provide such an interface
        localDataSource: LocalDataSource
    ) = LocalRepositoryImp(localDataSource) as LocalRepository
}