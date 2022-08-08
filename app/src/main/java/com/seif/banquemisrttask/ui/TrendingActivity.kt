package com.seif.banquemisrttask.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.seif.banquemisrttask.R
import com.seif.banquemisrttask.databinding.TrendingMainBinding
import kotlinx.coroutines.withTimeout

class TrendingActivity : AppCompatActivity() {
    lateinit var binding: TrendingMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TrendingMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.shimmerFrameLayout.startShimmer()
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_sort_by_name -> {
            }
            R.id.menu_sort_by_stars -> {

            }
        }
        return true
    }
}