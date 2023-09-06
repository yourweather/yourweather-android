package com.umc.yourweather.presentation.calendardetailview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.umc.yourweather.R

class CalendarDetailViewEmptyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_detail_view_empty)

        val btnModify: Button = findViewById(R.id.button2)

        btnModify.setOnClickListener {
            val intent = Intent(this@CalendarDetailViewEmptyActivity, CalendarWeatherDetailActivity::class.java)
            startActivity(intent)
        }
    }
}