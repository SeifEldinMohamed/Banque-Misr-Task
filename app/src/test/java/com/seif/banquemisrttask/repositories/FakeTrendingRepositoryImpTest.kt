package com.seif.banquemisrttask.repositories

import androidx.compose.runtime.mutableStateOf
import com.google.common.truth.Truth.assertThat
import com.seif.banquemisrttask.data.datasources.localdatasource.entities.TrendingRepositoriesEntity
import com.seif.banquemisrttask.data.datasources.remotedatasource.dto.TrendingRepositoriesItem
import com.seif.banquemisrttask.domain.usecases.GetTrendingRepositoriesUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class FakeTrendingRepositoryImpTest {

    private lateinit var getTrendingRepositoriesUseCase: GetTrendingRepositoriesUseCase
    private lateinit var fakeRepository: FakeTrendingRepository
    private lateinit var reposFromDatabase: MutableList<TrendingRepositoriesEntity>
    private lateinit var reposFromNetwork: MutableList<TrendingRepositoriesItem>

    @Before
    fun setUp() {
        reposFromDatabase = createReposFromDataBase()
        reposFromNetwork = createReposFromNetwork()
        fakeRepository = FakeTrendingRepository(reposFromDatabase, reposFromNetwork)
        getTrendingRepositoriesUseCase = GetTrendingRepositoriesUseCase(fakeRepository)
    }

    @Test
    fun `sorting repos by name _returns correct sorted repos`() = runBlocking {
        val sortedRepos = fakeRepository.sortTrendingRepositoriesByName().first()
        for (i in 0..sortedRepos.size - 2) {
            assertThat(sortedRepos[i].name).isLessThan(sortedRepos[i + 1].name)
        }
    }

    @Test
    fun `sorting repos by stars _returns correct sorted repos`() = runBlocking {
        val sortedRepos = fakeRepository.sortTrendingRepositoriesByName().first()
        for (i in 0..sortedRepos.size - 2) {
            assertThat(sortedRepos[i].stars).isLessThan(sortedRepos[i + 1].stars)
        }
    }

    private fun createReposFromNetwork(): MutableList<TrendingRepositoriesItem> {
        val repos = mutableListOf<TrendingRepositoriesItem>()
        ('a'..'d').forEachIndexed { index, c ->
            repos.add(
                TrendingRepositoriesItem(
                    c.toString(),
                    c.toString(),
                    c.toString(),
                    index,
                    c.toString(),
                    c.toString(),
                    c.toString(),
                    index,
                    c.toString(),
                )
            )
        }
        repos.shuffle()
        return repos
    }

    private fun createReposFromDataBase(lastFetchTime: Long = System.currentTimeMillis()): MutableList<TrendingRepositoriesEntity> {
        val repos = mutableListOf<TrendingRepositoriesEntity>()
        ('a'..'d').forEachIndexed { index, c ->
            repos.add(
                TrendingRepositoriesEntity(
                    index,
                    c.toString(),
                    c.toString(),
                    c.toString(),
                    index,
                    c.toString(),
                    c.toString(),
                    c.toString(),
                    index,
                    c.toString(),
                    lastFetchTime, // 7:00 pm
                    false
                )
            )
        }
        repos.shuffle()
        return repos
    }
}