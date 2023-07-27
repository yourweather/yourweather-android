package com.umc.yourweather.presentation.adapter

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.umc.yourweather.presentation.CalendarFragment
import java.util.Calendar
import java.util.Date

class CalendarMonthAdapter (fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity){
    companion object{
        const val START_POSITION = Int.MAX_VALUE / 2
    }

    override fun getItemCount() : Int =  Int.MAX_VALUE

    override fun createFragment (position: Int): CalendarFragment {
        val itemId = getItemId(position).toInt()
        return CalendarFragment().apply {
            arguments = Bundle().apply {
                putInt("year", itemId / 100)
                putInt("month", itemId % 100)
                putString("id", itemId.toString())
            }
        }
    }

    override fun getItemId(position: Int): Long {
        var calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.add(Calendar.MONTH, position - START_POSITION)

        var currentYear = calendar.get(Calendar.YEAR)
        var currentMonth = calendar.get(Calendar.MONTH) + 1
        return (currentYear * 100L + currentMonth).toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        return 190000L <= itemId && itemId <= 220000L
    }
}