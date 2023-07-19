package com.umc.yourweather.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivityNicknameBinding
import com.umc.yourweather.databinding.ActivitySignInBinding

class Nickname : AppCompatActivity() {
    lateinit var binding: ActivityNicknameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityNicknameBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnNicknameNext.setOnClickListener{
            val mIntent = Intent(this, AnalysisActivity::class.java)
            startActivity(mIntent)
            finish()
        }

    }
}
