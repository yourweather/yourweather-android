package com.umc.yourweather.presentation

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import com.umc.yourweather.R
import com.umc.yourweather.util.CalendarUtils.Companion.dpToPx
import java.util.Calendar
import java.util.Date

class CalendarDate @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    val calendarMonth: Int,
    val calendarYear: Int,
    val date: Date,
) : View(context, attrs, defStyleAttr) {
    val count = 1 // 데이터 개수
    lateinit var datePaint: Paint
    lateinit var temPaint: Paint
    var customDrawable: Drawable? = null
    var cal = Calendar.getInstance()

//    //**삭제예정!!!!!
//    private val borderPaint = Paint().apply {
//        color = Color.BLACK // 테두리 색상
//        style = Paint.Style.STROKE // 선 스타일 (STROKE는 외곽선)
//        strokeWidth = 5f // 선 두께
//    }

    interface OnDateClickListener {
        fun onDateClick(date: Date)
    }

    private var onDateClickListener: OnDateClickListener? = null

    fun setOnDateClickListener(listener: OnDateClickListener) {
        this.onDateClickListener = listener
    }

    init {
        // setCustomPadding() 삭제예정
        cal.time = date
        if (isSameMonth() == true) {
            context.withStyledAttributes(
                attrs,
                R.styleable.Calendar,
                defStyleAttr,
            ) {
                val dateFont = ResourcesCompat.getFont(context, R.font.pretendardregular)
                val tempFont = ResourcesCompat.getFont(context, R.font.pretendardsemibold)

                datePaint = TextPaint().apply {
                    textSize = dpToPx(context, 12).toFloat()
                    isAntiAlias = true
                    textAlign = Paint.Align.CENTER
                    typeface = dateFont
                }

                if (isLaterDay()) {
                    datePaint.color = Color.parseColor("#D1CAC6")
                } else {
                    datePaint.color = Color.parseColor("#2B2B2B")
                    if (count > 0) {
                        temPaint = TextPaint().apply {
                            textSize = dpToPx(context, 12).toFloat()
                            color = Color.parseColor("#2B2B2B")
                            typeface = Typeface.DEFAULT_BOLD
                            isAntiAlias = true
                            textAlign = Paint.Align.CENTER
                            typeface = tempFont
                        }
                        customDrawable = ContextCompat.getDrawable(context, R.drawable.ic_cloud)
                    }
                }
            }
        }
        setOnClickListener {
            // 클릭 이벤트가 발생했을 때 콜백으로 해당 날짜 전달
            onDateClickListener?.onDateClick(date)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), borderPaint)
        if (isSameMonth() == true) {
            val dateText = cal.get(Calendar.DAY_OF_MONTH).toString()
            val temp = "40" + "°"

            canvas?.drawText(dateText, (width / 2).toFloat(), dpToPx(context, 16).toFloat(), datePaint)
            // checkSize(datePaint, dateText)
            if (count > 0 && !isLaterDay()) {
                val drawableleft = (width / 2) - dpToPx(context, 24)
                val drawableright = drawableleft + dpToPx(context, 48)
                val drawableTop = dpToPx(context, 18).toInt()
                val drawableBoottom = drawableTop + dpToPx(context, 48)
                customDrawable?.let {
                    it.setBounds(drawableleft, drawableTop, drawableright, drawableBoottom)
                    it.draw(canvas!!)
                }
                canvas?.drawText(temp, (width / 2).toFloat(), dpToPx(context, 70).toFloat(), temPaint)
                // checkSize(temPaint, temp)
            }
        }
    }

    fun isSameMonth(): Boolean {
        val month = cal.get(Calendar.MONTH) + 1
        return month == calendarMonth
    }

    fun isLaterDay(): Boolean {
        val todayCalendar = Calendar.getInstance()
        return todayCalendar.before(cal)
    }
}