package com.seif.banquemisrttask.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.seif.banquemisrttask.R
import com.seif.banquemisrttask.databinding.TrendingMainBinding
import com.seif.banquemisrttask.ui.adapters.TrendingRepositoriesAdapter
import com.seif.banquemisrttask.util.NetworkResult
import com.seif.banquemisrttask.util.observeOnce
import com.seif.banquemisrttask.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.ScaleInTopAnimator
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TrendingActivity : AppCompatActivity() {
    lateinit var binding: TrendingMainBinding
    private val trendingAdapter: TrendingRepositoriesAdapter by lazy { TrendingRepositoriesAdapter() }
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TrendingMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        setUpRecyclerView()
        readDatabase()

        binding.btnRetry.setOnClickListener {
            requestApiData()
        }
        binding.swiptToRefresh.setOnRefreshListener {
            requestApiData()
            binding.swiptToRefresh.isRefreshing = false
        }
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            // we used this observeOnce extension function to handle the second trigger of this observer after caching the coming data in database
            // ( avoid reading from database after requesting data form api)
            mainViewModel.readTrendingRepositories.observeOnce(this@TrendingActivity) { database ->
                if (database.isNotEmpty()) {
                    Log.d("trending", "read data from database called")
                    trendingAdapter.addTrendingRepositories(database[0].trendingRepositories)
                    showRecyclerViewAndHideShimmerEffect()
                    binding.constraintRetry.visibility = View.GONE
                }
                else { // database is empty (first time)
                    requestApiData()
                }
            }
        }
    }

    private fun requestApiData() {
        Log.d("trending", "requestApiData called")
        mainViewModel.getTrendingRepositories()
        mainViewModel.trendingRepositoriesResponse.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    showRecyclerViewAndHideShimmerEffect()
                    binding.constraintRetry.visibility = View.GONE
                    response.data?.let {
                        trendingAdapter.addTrendingRepositories(it)
                        binding.rvTrending.scrollToPosition(0)
                        Log.d("main",it.toString())
                    }
                }
                is NetworkResult.Error -> {
                    showRetryAndHideShimmerEffectAndRecyclerView()
                    Log.d("main",response.message.toString())
                }
                is NetworkResult.Loading -> {
                    binding.constraintRetry.visibility = View.GONE
                    showShimmerEffectAndHideRecyclerView()
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.rvTrending.apply {
            layoutManager = LinearLayoutManager(this@TrendingActivity)
            adapter = trendingAdapter
        }
        binding.rvTrending.itemAnimator = ScaleInTopAnimator().apply {
            addDuration = 200
        }
        showShimmerEffectAndHideRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_sort_by_name -> {
                sortReposByName()
            }
            R.id.menu_sort_by_stars -> {
            }
        }
        return true
    }

    private fun sortReposByName() {
        lifecycleScope.launch {
            mainViewModel.readTrendingRepositories.observeOnce(this@TrendingActivity) {
                it?.let {
                    trendingAdapter.addTrendingRepositoriesItem(mainViewModel.sortReposByName(it))
                }
                binding.rvTrending.scrollToPosition(0)
            }
        }
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
}