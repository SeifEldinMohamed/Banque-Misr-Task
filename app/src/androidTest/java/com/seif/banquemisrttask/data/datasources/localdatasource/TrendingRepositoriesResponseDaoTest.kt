package com.seif.banquemisrttask.data.datasources.localdatasource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.seif.banquemisrttask.data.datasources.localdatasource.entities.TrendingRepositoriesEntity
import com.seif.banquemisrttask.data.getOrAwaitValueTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Rule
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@HiltAndroidTest // Annotation used for marking an Android emulator tests that require injection.
class TrendingRepositoriesResponseDaoTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    //  private lateinit var database: RepositoriesDatabase
    @Inject
    @Named("test_db")
    lateinit var database: RepositoriesDatabase

    private lateinit var dao: TrendingRepositoriesDao

    @Before
    fun setUp() {
        hiltRule.inject()
        dao = database.trendingRepositoriesDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertShoppingItem() = runBlocking {
        val repos: List<TrendingRepositoriesEntity> = createReposFromDataBase()
        dao.insertTrendingRepositories(repos)

        val trendingDataList: List<TrendingRepositoriesEntity> =
            dao.readTrendingRepositories().asLiveData().getOrAwaitValueTest()

        assertThat(trendingDataList).isEqualTo(repos)
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
                    lastFetchTime// 7:00 pm
                )
            )
        }
        return repos
    }

}

// the difference bet Room.inMemoryDatabaseBuilder() and Room.databaseBuilder() is that inMemoryDatabaseBuilder()
// in not a real database( save in RAM )

// allowMainThreadQueries() : we allow that we access this room database from the main thread.
// to make sure that all actions are executed one after another

// runBlocking : it's just a way to execute a coroutine in the main Thread, so that will actually block
// the main thread with suspend calls but we can execute suspend fun instead of that coroutine
//  runBlockingTest : optimize for test cases (will skip the delay fun to avoid delaying our test case).

// live data runs Asynchronous and that is a problem even we used getOrAwaitValue() fun so to solve
// this problem we need to tell junit to execute all teh code inside of this class one function after
// another in the same thread.
