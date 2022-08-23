package com.seif.banquemisrttask.di

import com.seif.banquemisrttask.data.datasources.localdatasource.LocalDataSource
import com.seif.banquemisrttask.data.datasources.remotedatasource.RemoteDataSource
import com.seif.banquemisrttask.data.repositoryImp.RepositoryImp
import com.seif.banquemisrttask.domain.repository.Repository
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
    fun provideRepository( // since we pass a ShoppingRepository Interface in our ViewModel Constructor dagger will look if we provide such an interface
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource
    ) = RepositoryImp(localDataSource,remoteDataSource) as Repository

}