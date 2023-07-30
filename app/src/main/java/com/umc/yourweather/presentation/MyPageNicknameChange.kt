package com.umc.yourweather.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivityMyPageNicknameChangeBinding

class MyPageNicknameChange : AppCompatActivity() {

    lateinit var binding: ActivityMyPageNicknameChangeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageNicknameChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}