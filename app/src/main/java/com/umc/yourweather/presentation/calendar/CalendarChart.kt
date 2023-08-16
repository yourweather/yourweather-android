package com.umc.yourweather.presentation.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.umc.yourweather.R
import com.umc.yourweather.data.remote.response.MemoDailyResponse
import com.umc.yourweather.util.ResourceUtils

@SuppressLint("ResourceAsColor")
class CalendarChart@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    val memoList: List<MemoDailyResponse.MemoItemResponse>,
) : View(context, attrs, defStyleAttr) {

    private var customDrawable: Drawable? = null
    private var linePaint = Paint()

    init {
        // 선을...만든다
        linePaint.color = R.color.char_line
        linePaint.strokeWidth = 5f

        val startX = 100f
        val startY = 100f
        val endX = 400f
        val endY = 400f

        // 원을... 만든다... 그냥 사진으로..
        customDrawable = ContextCompat.getDrawable(context, R.drawable.ic_calendar_graph)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }
}
