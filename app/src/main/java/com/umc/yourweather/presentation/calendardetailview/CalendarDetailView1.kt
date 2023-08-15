package com.umc.yourweather.presentation.calendardetailview

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.umc.yourweather.R
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.MemoDailyResponse
import com.umc.yourweather.data.service.MemoService
import com.umc.yourweather.data.service.WeatherService
import com.umc.yourweather.databinding.ActivityCalendarDetailView1Binding
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.di.UserSharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class CalendarDetailView1 : AppCompatActivity() {
    // 일기 변수 선언
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var binding: ActivityCalendarDetailView1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_detail_view1)

        binding = ActivityCalendarDetailView1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // 프래그먼트를 추가하고 초기화
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        // 프래그먼트 객체 생성
        val fragment1: Fragment = HorizontalScrollFragment()
        val fragment2: Fragment = ScrollviewFragment1()

        // 프래그먼트를 레이아웃 컨테이너에 추가
        fragmentTransaction.replace(R.id.fragment_container, fragment1)
        fragmentTransaction.replace(R.id.fragment_container, fragment2)

        // 프래그먼트 트랜잭션 완료
        fragmentTransaction.commit()

        val weatherId = intent.getIntExtra("weatherId", -1) // -1은 기본값, 원하는 값으로 설정해주세요

        Log.d("calendar weatherId detailview", "weather Id : $weatherId")

        // weatherId를 활용하여 API 요청 보내기
        if (weatherId != -1) {
            CalendarDetailView1Api(weatherId)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment2)
            .commit()

        val btnBack: ImageButton = findViewById(R.id.btn_calendardetailview1_back)

        btnBack.setOnClickListener {
            finish()
        }

        val btnModify: Button = findViewById(R.id.btn_calendardetailview1_modify)

        btnModify.setOnClickListener {
            val intent = Intent(this@CalendarDetailView1, CalendarDetailView3::class.java)
            startActivity(intent)
        }
    }

    private fun CalendarDetailView1Api(weatherId: Int) {
        val memoService = RetrofitImpl.authenticatedRetrofit.create(MemoService::class.java)
        val call = memoService.memoReturn(weatherId = weatherId)

        val service = RetrofitImpl.authenticatedRetrofit.create(WeatherService::class.java)

        call.enqueue(object : Callback<BaseResponse<MemoDailyResponse>> {
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
                            UserSharedPreferences.getUserNickname(this@CalendarDetailView1)

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
