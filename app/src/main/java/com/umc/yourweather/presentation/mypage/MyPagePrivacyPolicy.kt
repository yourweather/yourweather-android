package com.umc.yourweather.presentation.mypage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.databinding.ActivityMyPagePrivacyPolicyBinding

class MyPagePrivacyPolicy : AppCompatActivity() {
    lateinit var binding: ActivityMyPagePrivacyPolicyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPagePrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 버튼 클릭으로 되돌아가기
        binding.btnPrivacyPolicyBack.setOnClickListener {
            finish()
        }
        // 영역 클릭으로 되돌아가기
        binding.flPrivacyPolicyBack.setOnClickListener {
            finish()
        }
    }
}