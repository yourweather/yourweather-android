package com.umc.yourweather.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivityWrittenDetailListSunWeeklyBinding

class WrittenDetailListActivitySunWeekly : AppCompatActivity() {
    private lateinit var binding: ActivityWrittenDetailListSunWeeklyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityWrittenDetailListSunWeeklyBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnAllwrittenLeftArrow.setOnClickListener {
            finish()
        }
    }
}