package com.umc.yourweather.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivityCalendarBinding
import com.umc.yourweather.databinding.ActivityMainBinding
import com.umc.yourweather.presentation.adapter.CalendarMonthAdapter

class Calendar : AppCompatActivity() {
    lateinit var binding : ActivityCalendarBinding
    lateinit var  monthrAdapter : CalendarMonthAdapter

    var currentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)

        setContentView(binding.root)
        monthrAdapter = CalendarMonthAdapter(this)

        binding.vp2Calendar.adapter = monthrAdapter
        binding.vp2Calendar.setCurrentItem(CalendarMonthAdapter.START_POSITION, false)
        setDateInfo()

        //전으로 이동
        binding.btnCalendarBefore.setOnClickListener {
            currentPosition = binding.vp2Calendar.currentItem
            binding.vp2Calendar.setCurrentItem(currentPosition - 1, true)
            Log.d("앞으로 이동", "앞앞")
        }

        //후로 이동
        binding.btnCalendarNext.setOnClickListener {
            currentPosition = binding.vp2Calendar.currentItem
            binding.vp2Calendar.setCurrentItem(currentPosition + 1, true)

        }

        //연월띄우기
        binding.vp2Calendar.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                Log.d("onPageSelected", "앞앞")
                setDateInfo()
            }
        })
    }

    private fun setDateInfo(){
        val currentPosition = binding.vp2Calendar.currentItem
        val itemId = monthrAdapter.getItemId(currentPosition)
        val year = itemId / 100L
        val month = itemId % 100L
        binding.tvCalendarMonth.text = month.toString() +"월"
        binding.tvCalendarYear.text = year.toString() +"년"
    }
}