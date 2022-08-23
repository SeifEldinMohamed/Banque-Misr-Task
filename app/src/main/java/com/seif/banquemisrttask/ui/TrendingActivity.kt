package com.seif.banquemisrttask.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.seif.banquemisrttask.R
import com.seif.banquemisrttask.data.datasources.localdatasource.entities.TrendingRepositoriesEntity
import com.seif.banquemisrttask.databinding.TrendingMainBinding
import com.seif.banquemisrttask.ui.adapters.TrendingRepositoriesAdapter
import com.seif.banquemisrttask.util.NetworkResult
import com.seif.banquemisrttask.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.ScaleInTopAnimator

@AndroidEntryPoint
class TrendingActivity : AppCompatActivity() {
    private lateinit var binding: TrendingMainBinding
    private val trendingAdapter: TrendingRepositoriesAdapter by lazy { TrendingRepositoriesAdapter() }
    private lateinit var mainViewModel: MainViewModel
    private var trendingRepositoriesList: ArrayList<TrendingRepositoriesEntity>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TrendingMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""
        setUpRecyclerView()

        // handle configuration changes
        handleConfigurationChanges(savedInstanceState)

        binding.btnRetry.setOnClickListener {
            mainViewModel.forceFetchingData()
            observeApiData()
        }

        binding.swiptToRefresh.setOnRefreshListener {
            mainViewModel.forceFetchingData()
            observeApiData()
            binding.swiptToRefresh.isRefreshing = false
        }
    }

    private fun handleConfigurationChanges(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val trendingList =
                savedInstanceState.getParcelableArrayList<TrendingRepositoriesEntity>("trendingList")
            if (trendingList != null) {
                trendingAdapter.addTrendingRepositoriesItem(trendingList)
                trendingRepositoriesList = trendingList
                showRecyclerViewAndHideShimmerEffect()
                binding.constraintRetry.visibility = View.GONE
            } else {
                observeApiData()
            }
        } else {
            observeApiData()
        }
    }

    private fun observeApiData() {
        // this observer triggers when ( ex: loading, success, failure)
        mainViewModel.trendingRepositoriesResponse.observe(this) {
            handleNetworkResponse(it)
        }
    }

    private fun handleNetworkResponse(response: NetworkResult<List<TrendingRepositoriesEntity>>) {
        when (response) {
            is NetworkResult.Success -> {
                showRecyclerViewAndHideShimmerEffect()
                binding.constraintRetry.visibility = View.GONE

                response.data?.let {
                    trendingAdapter.addTrendingRepositoriesItem(it)
                    trendingRepositoriesList = it.toCollection(ArrayList())
                    binding.rvTrending.scrollToPosition(0)
                }
            }
            is NetworkResult.Error -> {
                showRetryAndHideShimmerEffectAndRecyclerView()
                Log.d("trending", "Error : ${response.message}")
            }
            is NetworkResult.Loading -> {
                Log.d("trending", "Loading.....")
                binding.constraintRetry.visibility = View.GONE
                showShimmerEffectAndHideRecyclerView()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        trendingRepositoriesList?.let {
            outState.putParcelableArrayList("trendingList", trendingRepositoriesList)
        }
    }

    private fun setUpRecyclerView() {
        binding.rvTrending.apply {
            layoutManager = LinearLayoutManager(this@TrendingActivity)
            adapter = trendingAdapter
            itemAnimator = ScaleInTopAnimator().apply {
                addDuration = 200
            }
        }
        showShimmerEffectAndHideRecyclerView()
    }

    private fun showRecyclerViewAndHideShimmerEffect() {
        binding.shimmerFrameLayout.apply {
            stopShimmer()
            visibility = View.GONE
        }
        binding.rvTrending.visibility = View.VISIBLE
    }

    private fun showShimmerEffectAndHideRecyclerView() {
        binding.shimmerFrameLayout.apply {
            visibility = View.VISIBLE
            startShimmer()
        }
        binding.rvTrending.visibility = View.GONE
    }

    private fun showRetryAndHideShimmerEffectAndRecyclerView() {
        binding.shimmerFrameLayout.apply {
            stopShimmer()
            visibility = View.GONE
        }
        binding.rvTrending.visibility = View.GONE
        binding.constraintRetry.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_sort_by_name -> {
                observeSortedReposByNameList()
            }
            R.id.menu_sort_by_stars -> {
                observeSortedReposByStarsList()
            }
        }
        return true
    }

    private fun observeSortedReposByNameList() {
        mainViewModel.sortReposByName.observe(this) {
            trendingAdapter.addTrendingRepositoriesItem(it)
            binding.rvTrending.scrollToPosition(0)
            trendingRepositoriesList = it.toCollection(ArrayList())
        }
    }

    private fun observeSortedReposByStarsList() {
        mainViewModel.sortReposByStars.observe(this) {
            trendingAdapter.addTrendingRepositoriesItem(it)
            binding.rvTrending.scrollToPosition(0)
            trendingRepositoriesList = it.toCollection(ArrayList())
        }
    }

}
