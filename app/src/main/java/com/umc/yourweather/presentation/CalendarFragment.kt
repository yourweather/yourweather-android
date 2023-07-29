package com.umc.yourweather.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umc.yourweather.R
import com.umc.yourweather.databinding.FragmentCalendarBinding
import java.util.Calendar
import java.util.Date

class CalendarFragment : Fragment() {
    lateinit var binding : FragmentCalendarBinding
    lateinit var cal : Calendar
    lateinit var todayCalendar: Calendar
    var dateList: MutableList<Date> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCalendarBinding.inflate(layoutInflater)
        cal = Calendar.getInstance()

        val year = arguments?.getInt("year")
        val month = arguments?.getInt("month")
        val id = arguments?.getString("id")

        cal.set(year!!, month!!-1, 1)
        getDate(year!!, month!!)
        binding.ctCalendarCustom.initCalendar(month!!, dateList)

        return binding.root
    }

    fun getDate(year : Int, month : Int){
        var dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)//이번 달 첫날의 요일 일요일부터(1~7)
        cal.add(Calendar.DATE, -(dayOfWeek-1))
        for(i in 1.. 42) {
            dateList.add(cal.time)
            cal.add(Calendar.DATE, 1)
        }
    }
}