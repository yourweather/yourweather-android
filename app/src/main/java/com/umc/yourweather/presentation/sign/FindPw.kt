package com.umc.yourweather.presentation.sign

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.databinding.ActivityFindPwBinding

class FindPw : AppCompatActivity() {
    lateinit var binding: ActivityFindPwBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindPwBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnFindpwNext.setOnClickListener {
        }
        binding.btnFindpwBack.setOnClickListener {
            finish()
        }
    }
}
