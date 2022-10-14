package com.seif.banquemisrttask.ui.viewmodel

import com.google.common.truth.Truth.assertThat
import com.seif.banquemisrttask.TestDispatchers
import com.seif.banquemisrttask.data.datasources.localdatasource.entities.TrendingRepositoriesEntity
import com.seif.banquemisrttask.domain.usecases.GetTrendingRepositoriesUseCase
import com.seif.banquemisrttask.getOrAwaitValueTest
import com.seif.banquemisrttask.repositories.FakeTrendingRepository
import com.seif.banquemisrttask.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainViewModelTest {
    private lateinit var mainViewModel: MainViewModel

//    @Inject
//    lateinit var test: TestDispatchers

    @Before
    fun setup(){

      //  Dispatchers.setMain(test.io)
        Dispatchers.setMain(StandardTestDispatcher())
        mainViewModel = MainViewModel(GetTrendingRepositoriesUseCase(FakeTrendingRepository()),FakeTrendingRepository())

    }
    private var shouldHasInternetConnection: Boolean = true

    private fun setShouldReturnNetworkError(value: Boolean) {
        shouldHasInternetConnection = !value
    }

    fun getTrendingRepositoriesSafeCall_HasInternetConnection(){
        if(shouldHasInternetConnection){
            runTest {
              //  viewModel.getTrendingRepositories()
            }
        }

    }

//    @Test
//    fun forceFetchData_successCall_returnsListOfRepositories(){
//
//        mainViewModel.forceFetchingData()
//
//        val value = mainViewModel.trendingRepositoriesResponse.getOrAwaitValueTest()
//
//        assertThat(value).isEqualTo(NetworkResult.Success(null))
//    }
//
//    @Test
//    fun forceFetchData_failCall_returnsError(){
//
//    }


}