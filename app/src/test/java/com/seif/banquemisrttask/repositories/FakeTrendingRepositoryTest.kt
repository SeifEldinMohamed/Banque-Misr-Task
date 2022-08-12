package com.seif.banquemisrttask.repositories

import org.junit.Assert
import org.junit.Test

class FakeTrendingRepositoryTest {
val fakeTrendingRepository = FakeTrendingRepository()
    @Test
    fun setShouldReturnNetworkError() {
    }

    @Test
    fun readTrendingRepositories() {
        Assert.assertEquals("ahmed", fakeTrendingRepository.getName())
    }

    @Test
    fun insertTrendingRepositories() {
    }

    @Test
    fun getTrendingRepositories() {
    }
}