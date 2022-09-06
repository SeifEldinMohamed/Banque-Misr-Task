package com.seif.banquemisrttask.domain.usecases

import com.google.common.truth.Truth.assertThat
import com.seif.banquemisrttask.data.datasources.localdatasource.entities.TrendingRepositoriesEntity
import com.seif.banquemisrttask.data.datasources.remotedatasource.dto.TrendingRepositoriesItem
import com.seif.banquemisrttask.repositories.FakeTrendingRepository
import com.seif.banquemisrttask.util.Constants
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetTrendingRepositoriesUseCaseTest {
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
    fun `true force fetch, get trending repositories from database after fetching from database in case of retry or swipe to refresh`() =
        runBlocking {

            val response =
                getTrendingRepositoriesUseCase(true).last() // we use last to get success state and avoid loading state that emit first
            assertThat(response.data).isEqualTo(reposFromDatabase.toList())

        }

    @Test
    fun `false force fetch, data in Database, last fetch time not exceeds 2 hours  _returns get trending repositories from database`() =
        runBlocking {

            val response =
                getTrendingRepositoriesUseCase(false).last() // we use last to get success state and avoid loading state that emit first
            assertThat(response.data).isEqualTo(reposFromDatabase.toList())

        }

    @Test
    fun `false force fetch, empty Database ( first time enter app ) _returns get trending repositories from database`() =
        runBlocking {

            reposFromDatabase =
                emptyList<TrendingRepositoriesEntity>().toMutableList() // empty Database
            reposFromNetwork = createReposFromNetwork()
            fakeRepository = FakeTrendingRepository(reposFromDatabase, reposFromNetwork)
            getTrendingRepositoriesUseCase = GetTrendingRepositoriesUseCase(fakeRepository)

            val response =
                getTrendingRepositoriesUseCase(false).last() // we use last to get success state and avoid loading state that emit first
            assertThat(response.data).isEqualTo(reposFromDatabase.toList())

        }

    @Test
    fun `false force fetch, data in Database, last fetch time exceeds 2 hours _returns get trending repositories from database after caching fetched data`() =
        runBlocking {

            reposFromDatabase =
                createReposFromDataBase((System.currentTimeMillis() + Constants.TWO_HOURS_INTERVAL)) // time in future (+2 hours form current)
            reposFromNetwork = createReposFromNetwork()
            fakeRepository = FakeTrendingRepository(reposFromDatabase, reposFromNetwork)
            getTrendingRepositoriesUseCase = GetTrendingRepositoriesUseCase(fakeRepository)

            val response =
                getTrendingRepositoriesUseCase(false).last() // we use last to get success state and avoid loading state that emit first
            assertThat(response.data).isEqualTo(reposFromDatabase.toList())

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