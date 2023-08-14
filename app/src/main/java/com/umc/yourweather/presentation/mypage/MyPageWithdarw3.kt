package com.umc.yourweather.presentation.mypage

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivityMyPageWithdraw3Binding
import com.umc.yourweather.presentation.sign.SignIn

class MyPageWithdarw3 : AppCompatActivity() {
    lateinit var binding : ActivityMyPageWithdraw3Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageWithdraw3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnWithdraw3Ok.setOnClickListener{
            val mIntent = Intent(this@MyPageWithdarw3, SignIn::class.java)
            mIntent.setFlags(FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(mIntent)
            finish()
        }
    }
}