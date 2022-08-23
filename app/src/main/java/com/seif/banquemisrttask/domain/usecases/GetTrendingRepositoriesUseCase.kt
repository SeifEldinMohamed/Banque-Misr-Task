package com.seif.banquemisrttask.domain.usecases

import com.seif.banquemisrttask.data.datasources.localdatasource.entities.TrendingRepositoriesEntity
import com.seif.banquemisrttask.domain.repository.Repository
import com.seif.banquemisrttask.util.NetworkResult
import kotlinx.coroutines.flow.Flow

class GetTrendingRepositoriesUseCase(
    private val repository: Repository
) {
     operator fun invoke(forceFetch:Boolean): Flow<NetworkResult<List<TrendingRepositoriesEntity>>> {
        return repository.getTrendingRepositories(forceFetch)
    }

}

// operator invoke function let me call this function from the class name to avoid writing boilerplate code