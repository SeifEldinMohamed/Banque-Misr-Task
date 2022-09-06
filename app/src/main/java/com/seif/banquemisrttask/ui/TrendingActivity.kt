package com.seif.banquemisrttask.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
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
    private var _binding: TrendingMainBinding? = null
    private val binding get() = _binding!!

    private val trendingAdapter: TrendingRepositoriesAdapter by lazy { TrendingRepositoriesAdapter() }
    private val mainViewModel: MainViewModel by viewModels()
    private var trendingRepositoriesList: ArrayList<TrendingRepositoriesEntity>? = null
    private var expandedSavedInstanceItemPosition: Int? = null
    private var isErrorState: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = TrendingMainBinding.inflate(layoutInflater)
        // binding.lifecycleOwner = this // bec we will use live data objects in our xml
        setContentView(binding.root)

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

        trendingAdapter.expandedPositionMutableLiveData.observe(this) {
            it?.let { position ->
                Log.d("rotate", "observe: $position")
                expandedSavedInstanceItemPosition = position
            }
        }
    }

    private fun handleConfigurationChanges(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val trendingList =
                savedInstanceState.getParcelableArrayList<TrendingRepositoriesEntity>("trendingList")
            val expandedPosition = savedInstanceState.getInt("expandedPosition")
            val isError = savedInstanceState.getBoolean("isErrorState")
            Log.d("rotate", "savedInstance: $expandedPosition")
            handleTrendingList(trendingList, expandedPosition, isError)
        } else {
            observeApiData()
        }
    }

    private fun handleTrendingList(
        trendingList: ArrayList<TrendingRepositoriesEntity>?,
        expandedPosition: Int,
        isError: Boolean
    ) {
        trendingList?.let {
            if (isError) {
                showRetryAndHideShimmerEffectAndRecyclerView()
                isErrorState = true
            } else {
                trendingAdapter.addTrendingRepositoriesItem(it)
                trendingAdapter.expandSavedInstanceItem(expandedPosition) // to expand saved item as before configuration change

                // to handle save new state and use it if user change configuration again
                trendingRepositoriesList = it
                expandedSavedInstanceItemPosition = expandedPosition
                isErrorState = false

                showRecyclerViewAndHideShimmerEffect()
                binding.constraintRetry.visibility = View.GONE
            }
            return
        }

        observeApiData()
    }

    private fun observeApiData() {
        // this observer triggers when ( ex: loading, success, failure)
//        mainViewModel.trendingRepositoriesResponse.observe(this) {
//            handleNetworkResult(it)
//        }
    }

    private fun handleNetworkResult(response: NetworkResult<List<TrendingRepositoriesEntity>>) {
        when (response) {
            is NetworkResult.Success -> {
                showRecyclerViewAndHideShimmerEffect()
                binding.constraintRetry.visibility = View.GONE

                response.data?.let {
                    trendingAdapter.addTrendingRepositoriesItem(it)
                    trendingAdapter.resetExpandingHandlers()

                    trendingRepositoriesList = it.toCollection(ArrayList())
                    isErrorState = false
                    // binding.rvTrending.scrollToPosition(0)
                }
            }
            is NetworkResult.Error -> {
                showRetryAndHideShimmerEffectAndRecyclerView()
                //  trendingRepositoriesList = ArrayList() // empty list when error occurred
                isErrorState = true
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
            outState.putParcelableArrayList("trendingList", it)
        }
        expandedSavedInstanceItemPosition?.let {
            outState.putInt("expandedPosition", it)
        }
        outState.putBoolean("isErrorState", isErrorState)
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
                if (!isErrorState)
                    observeSortedReposByNameList()
            }
            R.id.menu_sort_by_stars -> {
                if (!isErrorState)
                    observeSortedReposByStarsList()
            }
        }
        return true
    }

    private fun observeSortedReposByNameList() {
        mainViewModel.sortReposByName.observe(this) {
            trendingAdapter.addTrendingRepositoriesItem(it)
            trendingAdapter.resetExpandingHandlers() // to close expanded item if found
            binding.rvTrending.scrollToPosition(0)
            trendingRepositoriesList = it.toCollection(ArrayList())
        }
    }

    private fun observeSortedReposByStarsList() {
        mainViewModel.sortReposByStars.observe(this) {
            trendingAdapter.addTrendingRepositoriesItem(it)
            trendingAdapter.resetExpandingHandlers() // to close expanded item if found
            binding.rvTrending.scrollToPosition(0)
            trendingRepositoriesList = it.toCollection(ArrayList())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null // to avoid memory leaks
    }

}
