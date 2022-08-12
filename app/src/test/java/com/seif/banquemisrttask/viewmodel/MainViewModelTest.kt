package com.seif.banquemisrttask.viewmodel

import androidx.test.core.app.ApplicationProvider
import com.seif.banquemisrttask.repositories.FakeTrendingRepository
import org.junit.Before

class MainViewModelTest {
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup(){
        viewModel = MainViewModel(FakeTrendingRepository(), ApplicationProvider.getApplicationContext())

    }



}