package com.umc.yourweather.presentation.calendardetailview

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
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
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.MemoDailyResponse
import com.umc.yourweather.data.service.MemoService
import com.umc.yourweather.databinding.ActivityCalendarDetailView3Binding
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.di.UserSharedPreferences
import com.umc.yourweather.presentation.adapter.CalendarDetailviewDiaryAdapter
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ArrayList
import java.util.Calendar
import java.util.Locale

class CalendarDetailView3 : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarDetailView3Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_detail_view3)

        binding = ActivityCalendarDetailView3Binding.inflate(layoutInflater)
        setContentView(binding.root)

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


        val btnBack2: ImageButton = findViewById(com.umc.yourweather.R.id.btn_calendardetailview3_back)

        val weatherId = intent.getIntExtra("weatherId", -1) // -1은 기본값, 원하는 값으로 설정해주세요

        // weatherId를 활용하여 API 요청 보내기
        if (weatherId != -1) {
            CalendarDetailView3Api(weatherId)
        }

        btnBack2.setOnClickListener {
            val intent = Intent(this@CalendarDetailView3, CalendarDetailView1::class.java)
            finish()
        }


    }

    private fun CalendarDetailView3Api(weatherId: Int) {
        val memoService = RetrofitImpl.authenticatedRetrofit.create(MemoService::class.java)

        val call = memoService.memoReturn(weatherId = weatherId)

        call.enqueue(object : retrofit2.Callback<BaseResponse<MemoDailyResponse>> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<BaseResponse<MemoDailyResponse>>,
                response: Response<BaseResponse<MemoDailyResponse>>,
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()

                    if (response.code() == 200) { // 요청성공
                        val memoList = responseBody?.result?.memoList
                        val memoContentList = responseBody?.result?.memoContentList

                        val dateString = memoList?.firstOrNull()?.dateTime
                        val formatter =
                            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
                        val date = LocalDateTime.parse(dateString, formatter)

                        val month = date.monthValue
                        val year = date.year

                        // 사용자 닉네임 가져오기
                        val userNickname =
                            UserSharedPreferences.getUserNickname(this@CalendarDetailView3)

                        // inding.tvCalendarDetailview11.text =
                        // ("${month}월 ${day}일 ${userNickname}님의 날씨")
                    } else {
                        // 서버 응답은 성공했지만 데이터가 없는 경우 처리
                        Log.e("API Response", "No memo data for the requested date")
                    }
                } else {
                    // 서버 응답이 실패한 경우 처리
                    Log.e("API Response", "Failed to retrieve memo data")
                }
            }
                override fun onFailure(
            call: Call<BaseResponse<MemoDailyResponse>>,
            t: Throwable,
        ) {
            // 네트워크 요청 실패 처리
            Log.e("API Failure", "Error: ${t.message}", t)
            // 사용자에게 오류 메시지 표시
            val errorMessage = "네트워크 요청이 실패했습니다."
        }
        })

    }

}


