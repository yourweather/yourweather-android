package com.umc.yourweather.presentation

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.children
import com.umc.yourweather.util.CalendarUtils.Companion.DAYS_PER_WEEK
import com.umc.yourweather.util.CalendarUtils.Companion.WEEKS_PER_MONTH
import com.umc.yourweather.util.CalendarUtils.Companion.dpToPx
import java.time.LocalDate

class CalendarMonth @JvmOverloads constructor(
    context: Context,
    val attrs: AttributeSet? = null,
    val defStyleAttr: Int = 0,
) :
    ViewGroup(context, attrs, defStyleAttr) {

    private val onDateClickListener = object : CalendarDate.OnDateClickListener {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onDateClick(date: LocalDate) {
            Log.d("캘린더 클릭", "Clicked date: $date, 여기는 CalendarMonth")
            val mIntent = Intent(context, CalendarDetailView1::class.java)

            mIntent.putExtra("year", date.year.toString())
            mIntent.putExtra("month", date.monthValue.toString())
            mIntent.putExtra("date", date.dayOfMonth.toString())

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

    @RequiresApi(Build.VERSION_CODES.O)
    fun initCalendar(month: Int, list: MutableList<LocalDate>) {
        list.forEach {
            val calendarDateView = CalendarDate(
                context = context,
                thisDate = it,
                thisMonth = month,
            )
            // 클릭 이벤트 리스너를 설정하여 콜백 등록
            calendarDateView.setOnDateClickListener(onDateClickListener)
            addView(calendarDateView)
        }
        Log.d("캘린더 순서", "initCalendar")
    }
}
