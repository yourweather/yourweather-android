package com.umc.yourweather.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivityFindPwBinding
import com.umc.yourweather.databinding.ActivitySignInBinding

class FindPw : AppCompatActivity() {
    lateinit var binding : ActivityFindPwBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindPwBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnFindpwNext.setOnClickListener{

        }
    }
}