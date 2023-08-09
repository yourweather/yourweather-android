package com.umc.yourweather.presentation.sign

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivityFindPwEmailBinding

class FindPwEmail : AppCompatActivity() {
    lateinit var binding: ActivityFindPwEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindPwEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnFindPwEmailNext.setOnClickListener {
            val mIntent = Intent(this, ResetPw::class.java)
            startActivity(mIntent)
        }
        binding.btnFindPwEmailLeftArrow.setOnClickListener {
            finish()
        }
    }
}