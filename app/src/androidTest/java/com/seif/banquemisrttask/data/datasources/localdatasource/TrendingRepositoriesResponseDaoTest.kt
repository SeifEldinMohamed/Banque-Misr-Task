package com.seif.banquemisrttask.data.datasources.localdatasource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.seif.banquemisrttask.data.datasources.localdatasource.entities.TrendingRepositoriesEntity
import com.seif.banquemisrttask.data.getOrAwaitValueTest
import com.seif.banquemisrttask.data.datasources.remotedatasource.models.TrendingRepositoriesResponse
import com.seif.banquemisrttask.data.datasources.remotedatasource.models.TrendingRepositoriesItem
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.Rule
import javax.inject.Inject
import javax.inject.Named


@HiltAndroidTest // we make sure that all tests inside this class will run on the emulator and tell jUnit that this is instrumented tests
@SmallTest // to tell junit that we write here unit tests
class TrendingRepositoriesResponseDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
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

    @Before
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertShoppingItem() = runBlocking {
        val trendingRepositoriesList = arrayListOf<TrendingRepositoriesItem>(
            TrendingRepositoriesItem(
                "seif",
                "https://picsum.photos/seed/picsum/200/300",
                "hello, this seif's repository",
                1000,
                "Kotlin",
                "#000000",
                "My Kotlin Repository",
                2000,
                "https://picsum.photos/seed/picsum/200/300"
            ),
            TrendingRepositoriesItem(
                "mohamed",
                "https://picsum.photos/seed/picsum/200/300",
                "hello, this mohamed's repository",
                1000,
                "Java",
                "#000000",
                "My Java Repository",
                2000,
                "https://picsum.photos/seed/picsum/200/300"
            ),
            TrendingRepositoriesItem(
                "Ahmed",
                "https://picsum.photos/seed/picsum/200/300",
                "hello, this ahmed's repository",
                1000,
                "C++",
                "#000000",
                "My C++ Repository",
                2000,
                "https://picsum.photos/seed/picsum/200/300"
            )
        )
//        val trendingRepositoriesResponse = TrendingRepositoriesResponse()
//        trendingRepositoriesResponse.addAll(trendingRepositoriesList)
//        val trendingRepositoriesEntity = TrendingRepositoriesEntity(1, trendingRepositoriesResponse)
//
//        dao.insertTrendingRepositories(trendingRepositoriesEntity)
//
//        val trendingDataList: List<TrendingRepositoriesEntity> =
//            dao.readTrendingRepositories().asLiveData().getOrAwaitValueTest()
//
//        assertThat(trendingDataList[0].trendingRepositoriesResponse).isEqualTo(trendingRepositoriesResponse)
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
