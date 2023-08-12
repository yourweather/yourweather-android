package com.umc.yourweather.presentation.calendardetailview

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import com.umc.yourweather.data.remote.request.LoginRequest
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.TokenResponse
import com.umc.yourweather.data.remote.response.WeatherResponse
import com.umc.yourweather.data.service.LoginService
import com.umc.yourweather.data.service.WeatherService
import com.umc.yourweather.databinding.ActivityCalendarDetailView1Binding
import com.umc.yourweather.databinding.ActivityCalendarDetailviewModify1Binding
import com.umc.yourweather.di.App
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.presentation.BottomNavi
import com.umc.yourweather.presentation.adapter.CalendarDetailviewDiaryAdapter
import com.umc.yourweather.util.SignUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class CalendarDetailView1 : AppCompatActivity() {
//일기 변수 선언
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var binding: ActivityCalendarDetailView1Binding

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

        val btnModify: Button = findViewById(R.id.btn_calendardetailview1_modify)

        btnModify.setOnClickListener {
            val intent = Intent(this@CalendarDetailView1, CalendarDetailView3::class.java)
            startActivity(intent)
        }

    }
    private fun CalendarDetailView1Api(year: Int, month: Int, day: Int) {
        val service = RetrofitImpl.authenticatedRetrofit.create(WeatherService::class.java)

        val authToken = "calendardetailview1_auth_token" }}
       /* val call = service.getWeather(authToken, year ?: "", month ?: "", day ?: "")

        call.enqueue(object : Callback<BaseResponse<WeatherResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<WeatherResponse>>,
                response: Response<BaseResponse<WeatherResponse>>
            ) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()?.result
                    // 처리할 작업 수행
                    if (weatherResponse != null) {
                        val user = weatherResponse.user
                        binding.tvCalendarDetailview11.text = "${month}월 ${day}일 ${user}님의 날씨"
                    } else {
                        // 서버 응답은 성공했지만 데이터가 없는 경우 (예: 날씨 데이터 없음)
                        Log.e("WeatherActivity", "날씨 데이터가 없음")
                    }
                } else {
                    // 서버 응답이 실패한 경우
                    Log.e("WeatherActivity", "날씨 데이터 가져오기 실패")
                }
            }

            override fun onFailure(call: Call<BaseResponse<WeatherResponse>>, t: Throwable) {
                // 실패 처리
                Log.e("API Failure", "Error: ${t.message}", t)
            }
        })

    }

} */
