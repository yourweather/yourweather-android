package com.umc.yourweather.presentation.mypage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.databinding.ActivityMyPageUsePolicyBinding

class MyPageUsePolicy : AppCompatActivity() {
    lateinit var binding: ActivityMyPageUsePolicyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageUsePolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 버튼 클릭으로 되돌아가기
        binding.btnUsePolicyBack.setOnClickListener {
            finish() 
        }
        // 영역 클릭으로 되돌아가기
        binding.flUsePolicyBack.setOnClickListener {
            finish() 
        }
    }
}
