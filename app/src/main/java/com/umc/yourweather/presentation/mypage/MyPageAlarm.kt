package com.umc.yourweather.presentation.mypage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.databinding.ActivityMyPageAlarmBinding

class MyPageAlarm : AppCompatActivity() {
    private lateinit var binding: ActivityMyPageAlarmBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMyAlarm.setOnClickListener {
            finish()
        }
    }
}
