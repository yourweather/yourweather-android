package com.umc.yourweather.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.databinding.ActivityMyInfoBinding

class MyPageMyInfo : AppCompatActivity() {
    lateinit var binding: ActivityMyInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.flMyinfoBackbtn.setOnClickListener {
            finish()
        }

        binding.btnMyinfoPwChange.setOnClickListener {
            val mIntent = Intent(this, MyPagePwChange::class.java)
            startActivity(mIntent)
        }

        binding.btnMyinfoNicknameChange.setOnClickListener {
            val mIntent = Intent(this, MyPageNicknameChange::class.java)
            startActivity(mIntent)
        }
    }
}
