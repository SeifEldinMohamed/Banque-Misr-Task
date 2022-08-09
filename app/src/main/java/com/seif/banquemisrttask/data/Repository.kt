package com.seif.banquemisrttask.data

import com.seif.banquemisrttask.data.database.LocalDataSource
import com.seif.banquemisrttask.data.network.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped // we will have the same instance even when we rotate screen
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    localDataSource: LocalDataSource
) {
    val remote = remoteDataSource
    val local = localDataSource
}