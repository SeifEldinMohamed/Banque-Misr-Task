package com.seif.banquemisrttask.domain.usecases

import com.seif.banquemisrttask.domain.repository.RemoteRepository

class ShouldFetchDataUseCase(
    private val remoteRepository: RemoteRepository
) {
    operator fun invoke(): Boolean {
        return remoteRepository.shouldFetchData()
    }
}