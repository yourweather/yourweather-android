package com.umc.yourweather.presentation

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.umc.yourweather.R

class CalendarDetailView3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_detail_view3)
        val textView1: TextView = findViewById(R.id.textView)
        val textView3: TextView = findViewById(R.id.textView3)
        val btnBack2 : ImageButton = findViewById(com.umc.yourweather.R.id.imageButton)

        val year = intent.getStringExtra("year")
        val month = intent.getStringExtra("month")
        val date = intent.getStringExtra("date")

        textView1.text = "${month}월 ${date}일 이다은님의 날씨"
        textView3.text = "${month}월 ${date}일의 일기"

        btnBack2.setOnClickListener {
            val intent= Intent(this@CalendarDetailView3,CalendarDetailView1::class.java)
            startActivity(intent)
        }

        val myTextView1 = findViewById<TextView>(R.id.timeTextView1)
        val myTextView2 = findViewById<TextView>(R.id.timeTextView2)
        val myTextView3 = findViewById<TextView>(R.id.timeTextView3)
        val myTextView4 = findViewById<TextView>(R.id.timeTextView4)
        val myTextView5 = findViewById<TextView>(R.id.timeTextView5)

        myTextView1.paintFlags = myTextView1.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        myTextView2.paintFlags = myTextView1.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        myTextView3.paintFlags = myTextView1.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        myTextView4.paintFlags = myTextView1.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        myTextView5.paintFlags = myTextView1.paintFlags or Paint.UNDERLINE_TEXT_FLAG

    }
}