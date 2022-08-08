package com.seif.banquemisrttask.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.seif.banquemisrttask.databinding.TrendingMainBinding

class TrendingActivity : AppCompatActivity() {
    lateinit var binding: TrendingMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TrendingMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.shimmerFrameLayout.startShimmer()
    }
}