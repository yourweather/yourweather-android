package com.umc.yourweather.presentation

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.children
import com.umc.yourweather.util.CalendarUtils.Companion.DAYS_PER_WEEK
import com.umc.yourweather.util.CalendarUtils.Companion.WEEKS_PER_MONTH
import com.umc.yourweather.util.CalendarUtils.Companion.dpToPx
import java.util.Calendar
import java.util.Date

class CalendarMonth @JvmOverloads constructor(
    context: Context,
    val attrs: AttributeSet? = null,
    val defStyleAttr: Int = 0,
) :
    ViewGroup(context, attrs, defStyleAttr) {

    private val onDateClickListener = object : CalendarDate.OnDateClickListener {
        override fun onDateClick(date: Date) {
            Log.d("캘린더 클릭", "Clicked date: $date, 여기는 CalendarMonth")
            val mIntent = Intent(context, SignIn::class.java)
            val cal = Calendar.getInstance()
            cal.time = date

            var year = cal.get(Calendar.YEAR).toString()
            var month = (cal.get(Calendar.MONTH) + 1).toString()
            var date = cal.get(Calendar.DAY_OF_MONTH).toString()

            mIntent.putExtra("year", year)
            mIntent.putExtra("month", month)
            mIntent.putExtra("date", date)

            Toast.makeText(context, "캘린더 클릭 확인 ${year}년 ${month}월 ${date}일", Toast.LENGTH_SHORT).show()
            context.startActivity(mIntent)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.d("캘린더 순서", "onMeasure")
    }

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        val marginHorizontal = dpToPx(context, 16) // 좌우 마진
        val marginVertical = dpToPx(context, 12) // 상하 마진 크기 (픽셀 단위)
        var cWidth = (width - (DAYS_PER_WEEK - 1) * marginHorizontal) / DAYS_PER_WEEK
        var cHeight = (height - (WEEKS_PER_MONTH - 1) * marginVertical) / WEEKS_PER_MONTH

        children.forEachIndexed { index, view ->
            val left = (index % DAYS_PER_WEEK) * (cWidth + marginHorizontal)
            val top = (index / DAYS_PER_WEEK) * (cHeight + marginVertical)
            // if(index)
            view.layout(left, top, left + cWidth, top + cHeight)
        }
        Log.d("캘린더 순서", "onLayout")
    }

    fun initCalendar(year: Int, month: Int, list: MutableList<Date>) {
        list.forEach {
            val calendarDateView = CalendarDate(
                context = context,
                date = it,
                calendarYear = year,
                calendarMonth = month,
            )
            // 클릭 이벤트 리스너를 설정하여 콜백 등록
            calendarDateView.setOnDateClickListener(onDateClickListener)
            addView(calendarDateView)
        }
        Log.d("캘린더 순서", "initCalendar")
    }
}
