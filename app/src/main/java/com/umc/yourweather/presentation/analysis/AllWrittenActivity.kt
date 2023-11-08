package com.umc.yourweather.presentation.analysis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivityAllWrittenBinding

class AllWrittenActivity : AppCompatActivity() {

    lateinit var binding: ActivityAllWrittenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAllWrittenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.rlBtn.setOnClickListener {
            finish()
        }
    }
}