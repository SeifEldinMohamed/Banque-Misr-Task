package com.seif.banquemisrttask.ui.viewmodel

import androidx.test.core.app.ApplicationProvider
import com.seif.banquemisrttask.repositories.FakeTrendingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before

class MainViewModelTest {
    private lateinit var viewModel: MainViewModel


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup(){
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = MainViewModel(FakeTrendingRepository(), ApplicationProvider.getApplicationContext())

    }
    private var shouldHasInternetConnection: Boolean = true

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldHasInternetConnection = value
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getTrendingRepositoriesSafeCall_HasInternetConnection(){

        if(shouldHasInternetConnection){
            runTest {
                viewModel.getTrendingRepositories()
            }
        }

    }





}