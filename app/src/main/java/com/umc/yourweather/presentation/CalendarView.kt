package com.umc.yourweather.presentation

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.PopupWindow
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivityCalendarBinding
import com.umc.yourweather.entity.CalendarDateInfo
import com.umc.yourweather.presentation.adapter.CalendarMonthAdapter
import com.umc.yourweather.util.CalendarUtils.Companion.dpToPx
import java.time.LocalDate

class CalendarView : AppCompatActivity() {
    lateinit var binding: ActivityCalendarBinding
    lateinit var monthrAdapter: CalendarMonthAdapter
    lateinit var popupWindow: PopupWindow
    lateinit var calendarDateInfo: CalendarDateInfo
    var currentPosition = 0

    @RequiresApi(Build.VERSION_CODES.O)
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
        val itemId = monthrAdapter.getItemId(currentPosition).toInt()

        calendarDateInfo = CalendarDateInfo(itemId / 100, itemId % 100)
        binding.tvCalendarMonth.text = calendarDateInfo.month.toString() + "월"
        binding.tvCalendarYear.text = calendarDateInfo.year.toString() + "년"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun showPopupWindow(anchorView: View) {
        val popupView = LayoutInflater.from(this@CalendarView).inflate(R.layout.popupwindow_calendar, null)
        val listView = popupView.findViewById<ListView>(R.id.lv_caledar_popup)
        var moveDates = addMoveDate()

        val adapter = ArrayAdapter(this@CalendarView, android.R.layout.simple_list_item_1, moveDates.map { it.toString() })
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun addMoveDate(): ArrayList<CalendarDateInfo> {
        var moveDates = ArrayList<CalendarDateInfo>()

        val todayDate = LocalDate.now()
        // CurrentDate : 현재 위치하고 있는곳
        var currentDate = LocalDate.of(calendarDateInfo.year, calendarDateInfo.month, calendarDateInfo.date)

        // 현재 기준 이번달~현재 위치하고 있는 달 전까지
        var tmpDate = todayDate
        while (!tmpDate.monthValue.equals(currentDate.monthValue)) {
            moveDates.add(CalendarDateInfo(tmpDate.year, tmpDate.monthValue, 1))
            tmpDate = tmpDate.minusMonths(1)
        }

        // 현재 위치하고 있는달 ~ 그로부터 10년간의 달
        for (i in 1..120) {
            moveDates.add(CalendarDateInfo(tmpDate.year, tmpDate.monthValue, 1))
            tmpDate = tmpDate.minusMonths(1)
        }

        return moveDates
    }
}
