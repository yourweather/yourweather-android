package com.umc.yourweather.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.databinding.ActivityAllWrittenBinding

class AllWrittenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllWrittenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllWrittenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
