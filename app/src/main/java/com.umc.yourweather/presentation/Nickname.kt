package com.umc.yourweather.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.databinding.ActivityNicknameBinding

class Nickname : AppCompatActivity() {
    lateinit var binding: ActivityNicknameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNicknameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNicknameNext.setOnClickListener {
            val mIntent = Intent(this, AnalysisActivity::class.java)
            startActivity(mIntent)
            finish()
        } }
}
