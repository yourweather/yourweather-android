package com.umc.yourweather.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivityWrittenDetailListRainWeeklyBinding

class WrittenDetailListActivityRainWeekly : AppCompatActivity() {
    private lateinit var binding: ActivityWrittenDetailListRainWeeklyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityWrittenDetailListRainWeeklyBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnAllwrittenLeftArrow.setOnClickListener {
            finish()
        }
    }
}