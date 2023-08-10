package com.umc.yourweather.presentation.calendardetailview

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView
import com.umc.yourweather.R
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.umc.yourweather.presentation.adapter.CalendarDetailviewDiaryAdapter
import java.util.*


class CalendarDetailView1 : AppCompatActivity() {
    //일기 변수 선언
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_detail_view1)

        // 프래그먼트를 추가하고 초기화
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        // 프래그먼트 객체 생성
        val fragment1: Fragment = HorizontalScrollFragment()
        val fragment2: Fragment = ScrollviewFragment1()

        // 프래그먼트를 레이아웃 컨테이너에 추가
        fragmentTransaction.add(R.id.fragment_container, fragment1)
        fragmentTransaction.add(R.id.fragment_container, fragment2)

        // 프래그먼트 트랜잭션 완료
        fragmentTransaction.commit()

        val textView1: TextView = findViewById(R.id.tv_calendar_detailview1_1)

        val year = intent.getStringExtra("year")
        val month = intent.getStringExtra("month")
        val date = intent.getStringExtra("date")

        val btnBack: ImageButton = findViewById(R.id.btn_calendardetailview1_back)

        btnBack.setOnClickListener {
            finish()
        }

        textView1.text = "${month}월 ${date}일 이다은님의 날씨"

        val btnModify: Button = findViewById(R.id.btn_calendardetailview1_modify)

        btnModify.setOnClickListener {
            val intent = Intent(this@CalendarDetailView1, CalendarDetailView3::class.java)
            startActivity(intent)
        }

    }
}
