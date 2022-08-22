package com.seif.banquemisrttask.domain.usecases

import com.seif.banquemisrttask.data.datasources.localdatasource.entities.TrendingRepositoriesEntity
import com.seif.banquemisrttask.domain.repository.LocalRepository
import kotlinx.coroutines.flow.Flow

class ReadTrendingRepositoriesUseCase(
   private val localRepository: LocalRepository
   ) {
   operator fun invoke(): Flow<List<TrendingRepositoriesEntity>>{
      return localRepository.readTrendingRepositories()
   }
}