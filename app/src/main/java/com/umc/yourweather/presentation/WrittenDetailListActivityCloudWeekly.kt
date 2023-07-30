package com.umc.yourweather.presentation

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivityWrittenDetailListCloudWeeklyBinding

class WrittenDetailListActivityCloudWeekly : AppCompatActivity() {
    private lateinit var binding: ActivityWrittenDetailListCloudWeeklyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityWrittenDetailListCloudWeeklyBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnAllwrittenLeftArrow.setOnClickListener {
            finish()
        }
    }
}
