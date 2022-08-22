package com.seif.banquemisrttask.domain.usecases

import com.seif.banquemisrttask.data.datasources.remotedatasource.models.TrendingRepositories
import com.seif.banquemisrttask.domain.repository.LocalRepository
import com.seif.banquemisrttask.domain.repository.RemoteRepository
import com.seif.banquemisrttask.util.NetworkResult

class GetTrendingRepositoriesUseCase(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository) {

    suspend operator fun invoke(): NetworkResult<TrendingRepositories>? {
        val trendingRepositories = remoteRepository.getTrendingRepositories()
        if (trendingRepositories is NetworkResult.Success) {
            trendingRepositories.data?.let {
                localRepository.offlineCacheRepositories(it)
            }
        }
        return trendingRepositories
    }

}

// operator invoke function let me call this function from the class name to avoid writing boilerplate code