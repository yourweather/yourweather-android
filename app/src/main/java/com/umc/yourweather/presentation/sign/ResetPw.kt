package com.umc.yourweather.presentation.sign

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.umc.yourweather.databinding.ActivityPwResetBinding

class ResetPw : AppCompatActivity() {
    lateinit var binding: ActivityPwResetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPwResetBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}