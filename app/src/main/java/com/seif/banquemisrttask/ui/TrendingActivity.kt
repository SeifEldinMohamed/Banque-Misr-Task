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
import com.seif.banquemisrttask.data.network.models.TrendingRepositories
import com.seif.banquemisrttask.data.network.models.TrendingRepositoriesItem
import com.seif.banquemisrttask.data.sharedprefrence.AppSharedPreference
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
    private lateinit var binding: TrendingMainBinding
    private val trendingAdapter: TrendingRepositoriesAdapter by lazy { TrendingRepositoriesAdapter() }
    private lateinit var mainViewModel: MainViewModel

    private lateinit var trendingRepositoriesList: ArrayList<TrendingRepositoriesItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TrendingMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppSharedPreference.init(this)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""
        setUpRecyclerView()

        AppSharedPreference.readIsFirstTime("isFirstTime", true)?.let {
            if (it) {
                mainViewModel.getTrendingRepositories()
                AppSharedPreference.writeIsFirstTime("isFirstTime", false)
            }
        }


        if (savedInstanceState != null) {
            val trendingList =
                savedInstanceState.getParcelableArrayList<TrendingRepositoriesItem>("trendingList")
            if (trendingList != null) {
                trendingAdapter.addTrendingRepositoriesItem(trendingList)
                trendingRepositoriesList = trendingList
                showRecyclerViewAndHideShimmerEffect()
                binding.constraintRetry.visibility = View.GONE
            } else {
                observeDatabase()
            }
        } else {
            observeDatabase()
        }


        binding.btnRetry.setOnClickListener {
            mainViewModel.getTrendingRepositories()
            requestApiData()
        }
        binding.swiptToRefresh.setOnRefreshListener {
            mainViewModel.getTrendingRepositories()
            requestApiData()
            binding.swiptToRefresh.isRefreshing = false
        }
    }

    private fun observeDatabase() {
        // we used this observeOnce extension function to handle the second trigger of this observer after caching the coming data in database
        // ( avoid reading from database after requesting data form api)
        mainViewModel.readTrendingRepositories.observeOnce(this@TrendingActivity) { database ->
            if (database.isNotEmpty()) {
                Log.d("trending", "read data from database")

                trendingAdapter.addTrendingRepositoriesItem(database[0].trendingRepositories)
                trendingRepositoriesList = database[0].trendingRepositories

                showRecyclerViewAndHideShimmerEffect()
                binding.constraintRetry.visibility = View.GONE
            } else { // database is empty (first time)
                requestApiData()
            }
        }
    }

    private fun requestApiData() {
        Log.d("trending", "requestApiData called")
        // this observer triggers when ( ex: loading, success, failure)
        mainViewModel.trendingRepositoriesResponse.observe(this) { response: NetworkResult<TrendingRepositories> ->
            when (response) {
                is NetworkResult.Success -> {
                    showRecyclerViewAndHideShimmerEffect()
                    binding.constraintRetry.visibility = View.GONE

                    response.data?.let {
                        trendingAdapter.addTrendingRepositoriesItem(it)
                        trendingRepositoriesList = it

                        binding.rvTrending.scrollToPosition(0)
                        // Log.d("main", it.toString())
                    }
                }

                is NetworkResult.Error -> {
                    showRetryAndHideShimmerEffectAndRecyclerView()
                    // Log.d("main", response.message.toString())
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
                it?.let { trendingRepositories ->
                    val sortedTrendingRepositories =
                        mainViewModel.sortReposByName(trendingRepositories.toCollection(ArrayList()))
                    trendingAdapter.addTrendingRepositoriesItem(sortedTrendingRepositories)
                    trendingRepositoriesList = sortedTrendingRepositories
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("trendingList", trendingRepositoriesList)
    }

}
