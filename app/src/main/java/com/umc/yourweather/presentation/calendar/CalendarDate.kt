package com.umc.yourweather.presentation.calendar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import com.umc.yourweather.R
import com.umc.yourweather.util.CalendarUtils.Companion.checkTextSize
import com.umc.yourweather.util.CalendarUtils.Companion.dpToPx
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class CalendarDate @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    val thisMonth: Int,
    val thisDate: LocalDate,
    val dataList: List<Info>,
) : View(context, attrs, defStyleAttr) {
    val count = 1 // 데이터 개수
    lateinit var datePaint: Paint
    lateinit var temPaint: Paint
    var thisInfo: Info? = dataList.maxByOrNull { it.temper }
    var customDrawable: Drawable? = null

//    //**삭제예정!!!!!
//    private val borderPaint = Paint().apply {
//        color = Color.BLACK // 테두리 색상
//        style = Paint.Style.STROKE // 선 스타일
//        strokeWidth = 5f // 선 두께
//    }

    interface OnDateClickListener {
        fun onDateClick(date: LocalDate)
    }

    private var onDateClickListener: OnDateClickListener? = null

    fun setOnDateClickListener(listener: OnDateClickListener) {
        this.onDateClickListener = listener
    }

    init {
        // setCustomPadding() 삭제예정
        // 가장 높은 온도

        Log.d("date 클래스 ", "${thisDate.year}, ${thisDate.monthValue}, ${thisDate.dayOfMonth}")
        if (thisMonth == thisDate.monthValue) {
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
                    if (thisInfo != null) {
                        temPaint = TextPaint().apply {
                            textSize = dpToPx(context, 12).toFloat()
                            color = Color.parseColor("#2B2B2B")
                            isAntiAlias = true
                            textAlign = Paint.Align.CENTER
                            typeface = tempFont
                        }
                        customDrawable = setDrawable(thisInfo!!.weather)
                    }
                    setOnClickListener {
                        // 클릭 이벤트가 발생했을 때 콜백으로 해당 날짜 전달
                        onDateClickListener?.onDateClick(thisDate)
                    }
                }
            }
        }
    }

    fun setDrawable(weather: String): Drawable? {
        when (weather) {
            "sunny" -> {
                return ContextCompat.getDrawable(context, R.drawable.ic_sun)
            }
            "cloudy" -> {
                return ContextCompat.getDrawable(context, R.drawable.ic_cloud)
            }
            "lightning" -> {
                return ContextCompat.getDrawable(context, R.drawable.ic_thunder)
            }
            else -> {
                return ContextCompat.getDrawable(context, R.drawable.ic_rain)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), borderPaint)
        if (thisMonth == thisDate.monthValue) {
            val dateText = thisDate.dayOfMonth.toString()
            val temp = "${thisInfo?.temper}°"

            if (LocalDate.now() == thisDate) {
                circlePaint(dateText, canvas)
                datePaint.color = Color.WHITE
            }

            canvas?.drawText(
                dateText,
                (width / 2).toFloat(),
                dpToPx(context, 16).toFloat(),
                datePaint,
            )
            if (thisInfo != null && !isLaterDay()) {
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun isLaterDay(): Boolean {
        val today = LocalDate.now()
        return today.isBefore(thisDate)
    }

    fun circlePaint(text: String, canvas: Canvas?) {
        val textBounds = checkTextSize(datePaint, text)
        val roundPaint = Paint()

        val x = (width / 2).toFloat()
        val y = dpToPx(context, 16).toFloat() - (textBounds.height() / 2)

        val radius = y

        roundPaint.color = Color.parseColor("#5E412F")

        // 원 그리기
        canvas?.drawCircle(x, y, radius, roundPaint)
    }
}
