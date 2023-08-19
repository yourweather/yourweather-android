package com.umc.yourweather.presentation.calendardetailview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivityCalendarModifyWeatherBinding
import com.umc.yourweather.databinding.ActivityCalendarPlusWeatherBinding
import com.umc.yourweather.di.UserSharedPreferences

class CalendarModifyWeatherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarModifyWeatherBinding
    private lateinit var editText: AppCompatEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarModifyWeatherBinding.inflate(layoutInflater)

        setContentView(binding.root)


        // 닉네임 적용
        val userNickname = UserSharedPreferences.getUserNickname(this)

        binding.tvDetailviewModify2Title1.text = "$userNickname 님의 감정 상태"
        binding.tvDetailviewModify2Title2.text = "$userNickname 님의 감정 온도"
        binding.tvDetailviewModify2Title3.text = "$userNickname 님의 일기"

        editText = binding.editText as AppCompatEditText

    }
}
