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

                        // 서버에서 받아온 데이터
                        val memoList = responseBody?.result?.memoList
                        val memoContentList = responseBody?.result?.memoContentList

                        Log.d("캘린더 디테일 뷰 데이터 memoList", "$memoList")
                        Log.d("캘린더 디테일 뷰 데이터 memoContentLis", "$memoContentList")
                        Log.e("API Response", "Failed to retrieve memo data")

                        /* memoList의 첫번째 데이터의 시간을 가져오는 예시
                        "2023-08-16T01:09:58"라 T 기준으로 나눔
                        val dateString = memoList?.firstOrNull()?.dateTime

                        val date = dateString?.split("T")?.get(0)
                        val hour = dateString?.split("T")?.get(1)

                        Log.d("캘린더 날짜", "$date")
                        Log.d("캘린더 시간 ", "$hour")
                        // 사용자 닉네임 가져오기

                        */
                        //val creationDataTime=memoList.da
                        //LocalDateTime.parse(creationDataTime)
                       // val userNickname =
                          ///  UserSharedPreferences.getUserNickname(this@CalendarDetailView1)

                        // inding.tvCalendarDetailview11.text =
                        // ("${month}월 ${day}일 ${userNickname}님의 날씨")

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
//                        val firstMemoContent = memoContentList[0] // Use the first memo content item
//                            val memoId = firstMemoContent.memoId
//                            val creationDatetime = firstMemoContent.dateTime
//                            val content = firstMemoContent.status
//                            // 여기에서 memoItem의 필드 값을 활용하여 작업 수행
//                            viewAdapter = CalendarDetailviewDiaryAdapter(memoContentList)
//
//                            // Initialize the RecyclerView and its components
//                            recyclerView = rootView.findViewById<RecyclerView>(R.id.rv_scrollview_fragment1_1)
//                            viewManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)
//
//
//                            // Configure the RecyclerView
//                            recyclerView.apply {
//                                // Improve performance if the layout size of the RecyclerView doesn't change
//                                setHasFixedSize(true)
//                                // Use a linear layout manager
//                                layoutManager = viewManager
//                                // Specify an adapter
//                                adapter = viewAdapter
//                            }
//
//                            val dateString = firstMemoContent.dateTime
//                            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
//                            val date1 = dateFormat.parse(dateString.toString())
//                            val date2 = dateFormat.parse(creationDatetime)
//                            val calendar = Calendar.getInstance()
//                            calendar.time = date1
//
//
//                            val month = calendar.get(Calendar.MONTH) + 1 // 월은 0부터 시작하므로 +1을 해줌
//                            val day = calendar.get(Calendar.DAY_OF_MONTH)
//
//                            binding.tvScrollviewFragment11.text=("${month}월 ${day}일의 일기" )
//
//                            // Apply the desired time format
//                            val outputFormat = SimpleDateFormat("a h시", Locale.US)
//                            val formattedTime = outputFormat.format(date2)
//
//                            binding.tvScrollviewFragmentTime.text=formattedTime
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
