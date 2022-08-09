package com.seif.banquemisrttask.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.seif.banquemisrttask.R
import com.seif.banquemisrttask.databinding.TrendingMainBinding
import com.seif.banquemisrttask.ui.adapters.TrendingRepositoriesAdapter
import com.seif.banquemisrttask.util.NetworkResult
import com.seif.banquemisrttask.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

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
        requestApiData()
    }

    private fun requestApiData() {
        mainViewModel.getTrendingRepositories()
        mainViewModel.trendingRepositoriesResponse.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    showRecyclerViewAndHideShimmerEffect()
                    response.data?.let {
                        trendingAdapter.addTrendingRepositories(it)
                        Log.d("main",it.toString())
                    }
                }
                is NetworkResult.Error -> {
                     showRecyclerViewAndHideShimmerEffect()
                    // show retry button to user
                    Snackbar.make(binding.root, response.message.toString(), Snackbar.LENGTH_LONG)
                        .show()
                    Log.d("main",response.message.toString())
                }
                is NetworkResult.Loading -> {
                    showShimmerEffectAndHideRecyclerView()
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.rvTrending.apply {
            layoutManager = LinearLayoutManager(this@TrendingActivity)
            adapter = trendingAdapter
           // addItemDecoration(DividerItemDecoration(this@TrendingActivity, OrientationHelper.VERTICAL))
        }
        showShimmerEffectAndHideRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d("main", "created")
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_sort_by_name -> {
            }
            R.id.menu_sort_by_stars -> {

            }
        }
        return true
    }

    private fun showRecyclerViewAndHideShimmerEffect() {
        binding.shimmerFrameLayout.apply {
            visibility = View.GONE
            stopShimmer()
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
}