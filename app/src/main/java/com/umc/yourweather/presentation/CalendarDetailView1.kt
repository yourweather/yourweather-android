package com.umc.yourweather.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.R

class CalendarDetailView1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_detail_view1)

        val textView1: TextView = findViewById(R.id.textView)
        val textView3: TextView = findViewById(R.id.textView3) // 선언
        val year = intent.getStringExtra("year")
        val month = intent.getStringExtra("month")
        val date = intent.getStringExtra("date")

        val btnBack: ImageButton = findViewById(R.id.imageButton)

        btnBack.setOnClickListener {
            finish()
        }

        textView1.text = "8월 1일 이다은님의 날씨"
        textView3.text = "8월 1일의 일기"

        val btnModify: Button = findViewById(R.id.button)

        btnModify.setOnClickListener {
            val intent = Intent(this@CalendarDetailView1, CalendarDetailView3::class.java)
            startActivity(intent)
        }
    }
}
