package com.umc.yourweather.presentation.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import com.umc.yourweather.R

@SuppressLint("ResourceAsColor")
class CalendarChartGraph@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    val temper: List<Int>,
    val memoListWidth: Int,
) : View(context, attrs, defStyleAttr) {
// 가로 73dp 마진 양 각각 11dp
//    //87dp 만큼 떨어지면 댐...가로...
    private var ciclePaint = Paint()
    private var linePaint = Paint()

    init {
        // 선을...만든다
        linePaint.color = R.color.char_line
        linePaint.strokeWidth = 5f

        val startX = 100f
        val startY = 100f
        val endX = 400f
        val endY = 400f
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDraw(canvas: Canvas?) {
        // drawCircle(canvas)
        super.onDraw(canvas)
    }

//    fun drawCircle(canvas: Canvas?) {
//        // x좌표는..
//        // 48dp부터, 크기 8dp..
//        // 점찍기.. 온도 개수만큼 찍으면 댐
//        var centerX = 0F
//        var centerY = 0F
//        Log.d("여기오냐머노..?", "${temper}")
//        for (i in 0 until temper.size) {
//
//            Log.d("여기오냐y", "${(width / 100) * temper[i]}")
//            Log.d("여기요냐?x ", "$centerX")
//            centerX += dpToPx(context, memoListWidth / 2).toFloat()
//            centerY = dpToPx(context, (width / 100) * temper[i]).toFloat()
//            val radius = dpToPx(context, 200).toFloat()
//
//            // 테두리..
//            ciclePaint.color = Color.parseColor("#525252")
//            ciclePaint.style = Paint.Style.STROKE
//            ciclePaint.strokeWidth = 5f
//            canvas?.drawCircle(centerX, centerY, radius, ciclePaint)
//
//            // 내부 채우기.....
//            ciclePaint.color = Color.parseColor("#FFFFFF")
//            ciclePaint.style = Paint.Style.FILL
//            canvas?.drawCircle(centerX, centerY, radius, ciclePaint)
//        }
}
