package com.umc.yourweather.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.databinding.ActivityMyPageNicknameChangeBinding
import com.umc.yourweather.util.NicknameUtils

class MyPageNicknameChange : AppCompatActivity() {

    lateinit var binding: ActivityMyPageNicknameChangeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageNicknameChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // btn 클릭할 때마다 랜덤 닉네임 추천
        binding.btnMypageNicknameRefresh.setOnClickListener {
            binding.etMypageNicknameNickname.setText("")
            binding.etMypageNicknameNickname.hint = NicknameUtils.getRandomHintText()
        }
    }
}
