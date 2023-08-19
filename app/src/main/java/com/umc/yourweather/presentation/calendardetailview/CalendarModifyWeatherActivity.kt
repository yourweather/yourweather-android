package com.umc.yourweather.presentation.calendardetailview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivityCalendarModifyWeatherBinding
import com.umc.yourweather.databinding.ActivityCalendarPlusWeatherBinding

class CalendarModifyWeatherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarModifyWeatherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarModifyWeatherBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}
