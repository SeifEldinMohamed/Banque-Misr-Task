package com.seif.banquemisrttask.ui.compose

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seif.banquemisrttask.domain.model.TrendingRepository
import com.seif.banquemisrttask.domain.repository.Repository
import com.seif.banquemisrttask.domain.usecases.GetTrendingRepositoriesUseCase
import com.seif.banquemisrttask.ui.dispatcher.DispatcherProvider
import com.seif.banquemisrttask.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTrendingRepositoriesUseCase: GetTrendingRepositoriesUseCase,
    private val repository: Repository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {
    var previousSelectedPosition = -1

    private val _state = mutableStateOf(RepoUiState())
    val state: State<RepoUiState> = _state

    init {
        fetchingRepositories()
    }

    private fun fetchingRepositories(forceFetch: Boolean = false) {
        viewModelScope.launch(dispatchers.io) {
            getTrendingRepositoriesUseCase(forceFetch).onEach { result: NetworkResult<List<TrendingRepository>> ->
                when (result) {
                    is NetworkResult.Success -> {
                        _state.value = RepoUiState(repos = result.data ?: emptyList())
                    }
                    is NetworkResult.Error -> {
                        _state.value = RepoUiState(
                            error = result.message ?: "An unexpected error occured"
                        )
                    }
                    is NetworkResult.Loading -> {
                        Log.d("trending", "loading")
                        _state.value = RepoUiState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun forceFetchingData() {
        fetchingRepositories(true)
    }

    fun sortReposByName(){
        viewModelScope.launch(dispatchers.io){
            repository.sortTrendingRepositoriesByName().onEach {
                _state.value = RepoUiState(sortedReposByName = it )
            }.launchIn(viewModelScope)
        }
    }

    fun sortReposBStars(){
        viewModelScope.launch(dispatchers.io){
            repository.sortTrendingRepositoriesByStars().onEach {
                _state.value = RepoUiState(sortedReposByStars = it )
            }.launchIn(viewModelScope)
        }
    }

    fun handleExpandedState(reposList: List<TrendingRepository>, position:Int) {
        if (previousSelectedPosition == -1) {
            previousSelectedPosition = position
            reposList[position].isExpanded.value = true
        }
        else if (position == previousSelectedPosition) {
            reposList[position].isExpanded.value = !reposList[position].isExpanded.value
        }
        else { // not equal
            reposList[position].isExpanded.value = true
            reposList[previousSelectedPosition].isExpanded.value = false
            previousSelectedPosition = position
        }
    }
}