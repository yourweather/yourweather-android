package com.umc.yourweather.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.databinding.ActivityWithdrawPageBinding

class WithdrawPage : AppCompatActivity() {
    lateinit var binding: ActivityWithdrawPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWithdrawPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        binding.withdraw.setOnClickListener { }
        binding.cancel.setOnClickListener {
            finish()
        }
        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}
