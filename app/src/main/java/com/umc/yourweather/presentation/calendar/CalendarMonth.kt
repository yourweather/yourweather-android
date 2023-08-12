package com.umc.yourweather.presentation.calendar

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.children
import com.umc.yourweather.data.remote.response.MonthWeatherResponse
import com.umc.yourweather.presentation.calendardetailview.CalendarDetailView1
import com.umc.yourweather.util.CalendarUtils.Companion.DAYS_PER_WEEK
import com.umc.yourweather.util.CalendarUtils.Companion.WEEKS_PER_MONTH
import com.umc.yourweather.util.CalendarUtils.Companion.dpToPx
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarMonth @JvmOverloads constructor(
    context: Context,
    val attrs: AttributeSet? = null,
    val defStyleAttr: Int = 0,
) :
    ViewGroup(context, attrs, defStyleAttr) {

//    lateinit var weatherData: List<MonthWeatherResponse>

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
    fun initCalendar(year: Int, month: Int, list: MutableList<LocalDate>, weatherData: List<MonthWeatherResponse>) {
        // demo
        // var dataList = testCalendarData().weatherDatas

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        list.forEach { localdata ->
            var thisDate = weatherData.filter { LocalDate.parse(it.date, formatter) == localdata }.firstOrNull()

            Log.d("캘린더에 날짜 넣음 ", "$thisDate")
            val calendarDateView =
                CalendarDate(
                    context = context,
                    thisDate = localdata,
                    thisMonth = month,
                    dataList = thisDate,
                )

            calendarDateView.setOnDateClickListener(onDateClickListener)
            addView(calendarDateView)
        }
        Log.d("캘린더 순서", "initCalendar")
    }
}
