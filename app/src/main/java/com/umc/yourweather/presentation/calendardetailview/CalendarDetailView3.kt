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
        val btnPlus: ImageButton=findViewById(com.umc.yourweather.R.id.btn_calendardetailview3_plus)
        val weatherId = intent.getIntExtra("weatherId", -1) // -1은 기본값, 원하는 값으로 설정해주세요

        // weatherId를 활용하여 API 요청 보내기
        if (weatherId != -1) {
            CalendarDetailView3Api(weatherId)
        }

        btnBack2.setOnClickListener {
            val intent = Intent(this@CalendarDetailView3, CalendarDetailView1::class.java)
            finish()
        }
        btnPlus.setOnClickListener {
            val intent = Intent(this@CalendarDetailView3, CalendarDetailviewModify2::class.java)
            startActivity(intent)
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
                       //  ("${month}월 ${day}일 ${userNickname}님의 날씨")

                        //                        if (memoList.isNotEmpty()) {
//                            val firstMemoItem = memoList[0]
//                            val memoId = firstMemoItem.memoId
//
//                            binding.icHorizontalScrollWeather.setImageResource(
//                                getWeatherIconResource(firstMemoItem.status)
//                            )
//                            val temperature = firstMemoItem.temperature
//
//                            // 여기에서 memoItem의 필드 값을 활용하여 작업 수행
// // creationDatetime을 AM/PM 형식으로 표시하기 위해 포맷 변경
//                            val creationDatetime = formatDateToAmPm(firstMemoItem.dateTime)
//                            binding.tvHorizontalScrollTime.text = creationDatetime
//                            binding.icHorizontalScrollWeather.setImageResource(
//                                getWeatherIconResource(firstMemoItem.status)
//                            )
//
//                            // Create entries from the x and y values received from the server
//                            val entries: ArrayList<Entry> = ArrayList()
//
//                            for (i in firstMemoItem.dateTime.indices) {
//                                val xValue = i.toFloat() // Use index as x value
//                                val yValue =
//                                    firstMemoItem.temperature.toFloat() // Use firstMemoItem.temperature as y value
//                                entries.add(Entry(xValue, yValue))
//                            }
//
//                            // Create a LineDataSet from the entries
//                            val lineDataSet = LineDataSet(entries, "Temperature Data")
//
//                            // Customize the appearance of the LineDataSet
//                            lineDataSet.setCircleColor(Color.parseColor("#525252"))
//                            lineDataSet.setCircleHoleColor(Color.WHITE)
//                            lineDataSet.color = Color.parseColor("#F0A830")
//                            lineDataSet.setDrawHorizontalHighlightIndicator(false)
//                            lineDataSet.setDrawHighlightIndicators(false)
//                            lineDataSet.setDrawValues(false)
//
//                            // Add the LineDataSet to a List of ILineDataSet
//                            val dataSets: ArrayList<ILineDataSet> = ArrayList()
//                            dataSets.add(lineDataSet)
//
//                            // Create a LineData from the List of ILineDataSet
//                            val lineData = LineData(dataSets)
//
//                            // Get the LineChart view from the layout
//                            val lineChart: LineChart =
//                                binding.chartHorizontalScrollTempGraph // Make sure to update the ID
//
//                            // Set the LineData to the LineChart
//                            lineChart.data = lineData
//
//                            // Customize the appearance of the LineChart
//                            lineChart.setDrawBorders(false) // Hide chart borders
//                            lineChart.description.isEnabled = false // Hide chart description
//                            lineChart.legend.isEnabled = false // Hide chart legend
//                            lineChart.xAxis.isEnabled = false // Hide x-axis
//                            lineChart.axisLeft.isEnabled = false // Hide left y-axis
//                            lineChart.axisRight.isEnabled = false // Hide right y-axis
//                            lineChart.axisLeft.setDrawGridLines(false) // Hide horizontal grid lines
//                            lineChart.axisRight.setDrawGridLines(false) // Hide horizontal grid lines
//                            lineChart.xAxis.setDrawGridLines(false) // Hide vertical grid lines
//                            lineChart.setTouchEnabled(false) // Disable chart touch
//
//                            // Hide chart borders (graph frame)
//                            lineChart.setDrawBorders(false)
//
//                            // Refresh the LineChart to update the view
//                            lineChart.invalidate()
//
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


