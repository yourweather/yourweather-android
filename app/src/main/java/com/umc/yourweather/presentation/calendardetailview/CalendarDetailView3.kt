package com.umc.yourweather.presentation.calendardetailview

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
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

        call.enqueue(object : retrofit2.Callback<BaseResponse<BaseResponse<MemoDailyResponse>>> {
            override fun onResponse(
                call: Call<BaseResponse<BaseResponse<MemoDailyResponse>>>,
                response: Response<BaseResponse<BaseResponse<MemoDailyResponse>>>
            ) {
                if (response.isSuccessful) {
                    val outerResponse = response.body()?.result
                    val memoDailyResponse = outerResponse?.result

                    if (memoDailyResponse != null) {
                        // 여기에서 memoDailyResponse를 활용하여 UI 업데이트
                        val memoList = memoDailyResponse.memoList
                        val dateTime = memoDailyResponse.memoList
                        val memoContentList = memoDailyResponse.memoContentList
                        // 여기에서 사용자 정보 활용하여 작업 수행

                        val dateString = dateTime
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
                        val date = dateFormat.parse(dateString.toString())

                        val calendar = Calendar.getInstance()
                        calendar.time = date

                        val month = calendar.get(Calendar.MONTH) + 1 // 월은 0부터 시작하므로 +1을 해줌
                        val day = calendar.get(Calendar.DAY_OF_MONTH)
                        // 사용자 닉네임 가져오기
                        val userNickname =
                            UserSharedPreferences.getUserNickname(this@CalendarDetailView3)

                        binding.tvCalendarDetailview31.text =
                            ("${month}월 ${day}일 ${userNickname}님의 날씨")

                    }
                } else {
                    // 서버 응답이 실패한 경우 처리
                    Log.e("API Response", "Failed to retrieve memo data")
                }
            }

            override fun onFailure(
                call: Call<BaseResponse<BaseResponse<MemoDailyResponse>>>,
                t: Throwable
            ) {
                // 네트워크 요청 실패 처리
                Log.e("API Failure", "Error: ${t.message}", t)
                // 사용자에게 오류 메시지 표시
                val errorMessage = "네트워크 요청이 실패했습니다."
            }
        })

    }

}


