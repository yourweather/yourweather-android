package com.umc.yourweather.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.children
import com.umc.yourweather.util.CalendarUtils.Companion.DAYS_PER_WEEK
import com.umc.yourweather.util.CalendarUtils.Companion.WEEKS_PER_MONTH
import com.umc.yourweather.util.CalendarUtils.Companion.dpToPx
import java.util.Date

class CalendarMonth @JvmOverloads constructor(
    context: Context,
    val attrs: AttributeSet? = null,
    val defStyleAttr: Int = 0) :
    ViewGroup(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        val marginHorizontal = dpToPx(context, 16).toInt()//좌우 마진
        val marginVertical = dpToPx(context, 12).toInt() // 상하 마진 크기 (픽셀 단위)
        var cWidth = (width - (DAYS_PER_WEEK-1) * marginHorizontal) / DAYS_PER_WEEK
        var cHeight = (height - (WEEKS_PER_MONTH-1) * marginVertical) / WEEKS_PER_MONTH

        children.forEachIndexed { index, view ->
            val left = (index % DAYS_PER_WEEK) * (cWidth + marginHorizontal)
            val top = (index / DAYS_PER_WEEK) * (cHeight + marginVertical)
            //if(index)
            view.layout(left, top, left + cWidth, top + cHeight)
        }
    }

    fun initCalendar(month: Int, list: MutableList<Date>) {
        list.forEach {
            addView(
                CalendarDate(
                    context = context,
                    date = it,
                    calendarMonth = month
                )
            )
        }
    }
}