package com.seif.banquemisrttask.repositories

import com.seif.banquemisrttask.data.datasources.localdatasource.entities.TrendingRepositoriesEntity
import com.seif.banquemisrttask.data.datasources.remotedatasource.dto.TrendingRepositoriesItem
import com.seif.banquemisrttask.domain.model.TrendingRepository
import com.seif.banquemisrttask.domain.repository.Repository
import com.seif.banquemisrttask.domain.toTrendingRepositoriesEntityList
import com.seif.banquemisrttask.domain.toTrendingRepository
import com.seif.banquemisrttask.util.Constants
import com.seif.banquemisrttask.util.NetworkResult
import com.seif.banquemisrttask.util.networkBoundResource
import kotlinx.coroutines.flow.*

class FakeTrendingRepository(
    private var reposFromDataBase: MutableList<TrendingRepositoriesEntity> = mutableListOf(),
    private var reposFromNetwork: MutableList<TrendingRepositoriesItem> = mutableListOf(),
) : Repository { // make this class to test viewModel

    override fun getTrendingRepositories(forceFetch: Boolean): Flow<NetworkResult<List<TrendingRepository>>> {
        return networkBoundResource<List<TrendingRepository>, List<TrendingRepositoriesItem>>(
            query = {
                flow {
                    val reposEntity = reposFromDataBase
                    emit(reposEntity.toTrendingRepository())
                }
            },
            fetch = {
                 reposFromNetwork
            },
            saveFetchResult = {
                reposFromDataBase.addAll(it.toTrendingRepositoriesEntityList())
            },
            shouldFetch = {
                if(it.isEmpty() || forceFetch){
                    true
                }
                else{
                    it.last().fetchTimeStamp + Constants.TWO_HOURS_INTERVAL < System.currentTimeMillis()
                }
            }
        )
    }

    override fun sortTrendingRepositoriesByStars(): Flow<List<TrendingRepositoriesEntity>> {
        reposFromDataBase.sortBy {
            it.stars
        }
        return flow { emit(reposFromDataBase) }
    }

    override fun sortTrendingRepositoriesByName(): Flow<List<TrendingRepositoriesEntity>> {
        reposFromDataBase.sortBy {
            it.name
        }
        return flow { emit(reposFromDataBase) }
    }

}
