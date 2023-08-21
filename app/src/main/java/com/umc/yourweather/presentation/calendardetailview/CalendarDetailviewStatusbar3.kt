package com.umc.yourweather.presentation.calendardetailview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.umc.yourweather.R

class CalendarDetailviewStatusbar3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_detailview_statusbar3)

        val toolbar: Toolbar = findViewById(R.id.tb_calendar_detail_back3)
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로 가기 버튼 활성화
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_calendardetailview_back)

        toolbar.setNavigationOnClickListener {
            // 다른 화면으로 전환하는 코드
            // val intent = Intent(this, OtherActivity::class.java)
            startActivity(intent)
        }

    }
}