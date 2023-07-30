package com.umc.yourweather.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.databinding.ActivityUnwrittenDetailListBinding

class UnwrittenDetailListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUnwrittenDetailListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUnwrittenDetailListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAllwrittenLeftArrow.setOnClickListener {
            finish()
        }
    }
}
