package com.umc.yourweather.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivityWrittenDetailListThunderWeeklyBinding

class WrittenDetailListActivityThunderWeekly : AppCompatActivity() {
    private lateinit var binding: ActivityWrittenDetailListThunderWeeklyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
       binding = ActivityWrittenDetailListThunderWeeklyBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnAllwrittenLeftArrow.setOnClickListener {
            finish()
        }
    }
}