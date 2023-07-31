package com.umc.yourweather.presentation

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivityCalendarBinding
import com.umc.yourweather.presentation.adapter.CalendarMonthAdapter
import com.umc.yourweather.util.CalendarUtils.Companion.dpToPx

class CalendarView : AppCompatActivity() {
    lateinit var binding: ActivityCalendarBinding
    lateinit var monthrAdapter: CalendarMonthAdapter
    lateinit var popupWindow: PopupWindow

    var currentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)

        setContentView(binding.root)
        monthrAdapter = CalendarMonthAdapter(this)

        binding.vp2Calendar.adapter = monthrAdapter
        binding.vp2Calendar.setCurrentItem(CalendarMonthAdapter.START_POSITION, false)
        setDateInfo()

        // 전으로 이동
        binding.btnCalendarBefore.setOnClickListener {
            currentPosition = binding.vp2Calendar.currentItem
            binding.vp2Calendar.setCurrentItem(currentPosition - 1, true)
            Log.d("앞으로 이동", "앞앞")
        }

        // 후로 이동
        binding.btnCalendarNext.setOnClickListener {
            currentPosition = binding.vp2Calendar.currentItem
            binding.vp2Calendar.setCurrentItem(currentPosition + 1, true)
        }

        // 연월띄우기
        binding.vp2Calendar.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                Log.d("onPageSelected", "앞앞")
                setDateInfo()
            }
        })
        binding.btnCalendarYear.setOnClickListener {
            val anchorView = findViewById<View>(R.id.ll_calendar_year)
            showPopupWindow(anchorView)
        }
    }

    private fun setDateInfo() {
        val currentPosition = binding.vp2Calendar.currentItem
        val itemId = monthrAdapter.getItemId(currentPosition)
        val year = itemId / 100L
        val month = itemId % 100L
        binding.tvCalendarMonth.text = month.toString() + "월"
        binding.tvCalendarYear.text = year.toString() + "년"
    }

    fun showPopupWindow(anchorView: View) {
        val popupView = LayoutInflater.from(this@CalendarView).inflate(R.layout.popupwindow_calendar, null)
        val listView = popupView.findViewById<ListView>(R.id.lv_caledar_popup)
        var moveDates = addMoveDate()

        val adapter = ArrayAdapter(this@CalendarView, android.R.layout.simple_list_item_1, moveDates)
        listView.adapter = adapter

        val width = dpToPx(this@CalendarView, 190)
        val height = dpToPx(this@CalendarView, 462)

        popupWindow = PopupWindow(popupView, width, height, true)

        popupWindow.isOutsideTouchable = true
        popupWindow.showAsDropDown(anchorView)
        binding.viewBackgroundView.visibility = View.VISIBLE

        popupWindow.setOnDismissListener {
            binding.viewBackgroundView.visibility = View.INVISIBLE
        }
    }

    fun addMoveDate(): ArrayList<String> {
        var moveDates = ArrayList<String>()
        for (i in 1..120)
            moveDates.add("$i")
        return moveDates
    }
}
