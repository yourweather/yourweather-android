package com.umc.yourweather.presentation.calendar

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.umc.yourweather.databinding.FragmentCalendarBinding
import java.time.LocalDate
import java.util.Calendar

class CalendarFragment : Fragment() {
    lateinit var binding: FragmentCalendarBinding
    lateinit var cal: Calendar
    lateinit var todayCalendar: Calendar
    var dateList: MutableList<LocalDate> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCalendarBinding.inflate(layoutInflater)
        cal = Calendar.getInstance()

        val year = arguments?.getInt("year")
        val month = arguments?.getInt("month")
        val id = arguments?.getString("id")

        cal.set(year!!, month!! - 1, 1)
        getDate(year!!, month!!)
        Log.d("날짜확인확인", "$year $month")
        binding.ctCalendarCustom.initCalendar(month!!, dateList)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDate(year: Int, month: Int) {
        val firstDay = LocalDate.of(year, month, 1)
        // 이번 달 첫날의 요일 (월요일: 1, 화요일: 2, ..., 일요일: 7)
        val dayOfWeekValue = firstDay.dayOfWeek.value
        var n = 0

        if (dayOfWeekValue != 7) {
            n = dayOfWeekValue
        }

        var startDate = firstDay.minusDays(n.toLong())
        for (i in 1..42) {
            Log.d("날짜확인확인", "${startDate.year} ${startDate.monthValue}, ${startDate.dayOfMonth}")
            dateList.add(startDate)
            startDate = startDate.plusDays(1)
        }
    }
}
