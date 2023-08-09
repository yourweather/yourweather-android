package com.umc.yourweather.presentation.calendardetailview

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.umc.yourweather.R
import com.umc.yourweather.presentation.adapter.CalendarDetailviewDiaryAdapter
import java.util.ArrayList

class CalendarDetailView3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_detail_view3)

        // 프래그먼트를 추가하고 초기화
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        // 프래그먼트 객체 생성
        val fragment1: Fragment = HorizontalScrollFragment2()
        val fragment2: Fragment = ScrollviewFragment2()

        // 프래그먼트를 레이아웃 컨테이너에 추가
        fragmentTransaction.add(R.id.fragment_container, fragment1)
        fragmentTransaction.add(R.id.fragment_container, fragment2)

        // 프래그먼트 트랜잭션 완료
        fragmentTransaction.commit()

        val textView1: TextView = findViewById(R.id.textView)


        val btnBack2: ImageButton = findViewById(com.umc.yourweather.R.id.imageButton)

        val year = intent.getStringExtra("year")
        val month = intent.getStringExtra("month")
        val date = intent.getStringExtra("date")

        textView1.text = "${month}월 ${date}일 이다은님의 날씨"


        btnBack2.setOnClickListener {
            val intent = Intent(this@CalendarDetailView3, CalendarDetailView1::class.java)
            finish()
        }



    }
}

