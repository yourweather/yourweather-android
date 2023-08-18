package com.umc.yourweather.presentation.calendar

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.umc.yourweather.R
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.MemoDailyResponse
import com.umc.yourweather.data.service.MemoService
import com.umc.yourweather.databinding.ActivityCalendarDetailBinding
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.di.UserSharedPreferences
import com.umc.yourweather.presentation.adapter.CalendarDetailMemoContentAdapter
import com.umc.yourweather.presentation.adapter.CalendarDetailMemoListAdapter
import com.umc.yourweather.presentation.calendardetailview.CalendarDetailviewModify1
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CalendarDetail : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarDetailBinding

    var month: Int = 0
    var date: Int = 0

// 와이라노..
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val weatherId = intent.getIntExtra("weatherId", -1)
        val thisDate = intent.getStringExtra("date")

        val localDate = LocalDate.parse(thisDate)
        month = localDate.monthValue
        date = localDate.dayOfMonth

        if (weatherId == -1) {
            emptyView(thisDate)
        } else {
            CalendarDetailViewApi(weatherId, thisDate)
        }
    }

    private fun CalendarDetailViewApi(weatherId: Int, thisDate: String?) {
        val memoService = RetrofitImpl.authenticatedRetrofit.create(MemoService::class.java)
        val call = memoService.memoReturn(weatherId = weatherId)

        call.enqueue(object : Callback<BaseResponse<MemoDailyResponse>> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<BaseResponse<MemoDailyResponse>>,
                response: Response<BaseResponse<MemoDailyResponse>>,
            ) {
                val responseBody = response.body()
                if (response.isSuccessful) {
                    if (responseBody?.code == 200) {
                        val memoList = responseBody?.result?.memoList
                        var memoContentList = responseBody?.result?.memoContentList

                        if (memoList != null) {
                            memoList.map {
                                val dateTime = LocalDateTime.parse(it.creationDatetime)
                                val formatter = DateTimeFormatter.ofPattern("a h:mm")
                                val formattedTime = dateTime.format(formatter)
                                it.creationDatetime = formattedTime.replace("AM", "오전").replace("PM", "오후")
                                return@map
                            }
                        }

                        if (memoContentList != null) {
                            memoContentList.map {
                                val dateTime = LocalDateTime.parse(it.creationDatetime)
                                val formatter = DateTimeFormatter.ofPattern("a h")
                                val formattedTime = dateTime.format(formatter)

                                it.creationDatetime = formattedTime.replace("AM", "오전").replace("PM", "오후")
                                return@map
                            }
                            // 기록은 없는경우 null이 아니라 ""이다..
                            memoContentList = memoContentList.filter { !it.content.equals("") }
                            Log.d("calendarDetail memoContent 확인...", "$memoContentList")
                        }

                        calendarDetailView(memoList, memoContentList, thisDate)

                        Log.d("calendarDetail memoList", "$memoList")
                        Log.d("calendarDetail memoContentLis", "$memoContentList")
                        Log.e("calendarDetail API Response", "성공")
                    } else {
                        Log.d(
                            "calendarDetail",
                            "onResponse 오류: ${response?.toString()}",
                        )
                    }
                }
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onFailure(call: Call<BaseResponse<MemoDailyResponse>>, t: Throwable) {
                Log.d(
                    "calendarDetail",
                    "onFailure 오류: ${t.message?.toString()}",
                )
            }
        })
    }

    // 화면 출력 함수
    @RequiresApi(Build.VERSION_CODES.O)
    private fun calendarDetailView(
        memoList: List<MemoDailyResponse.MemoItemResponse>?,
        memoContent: List<MemoDailyResponse.MemoContentResponse>?,
        thisDate: String?,
    ) {
        if (memoList?.size == 0 && memoContent?.size == 0) {
            // 아무것도..없다
            emptyView(thisDate)
        } else if (memoList?.size!! > 0 && memoContent?.size == 0) {
            // 메모가 없다
            binding.rvCalendarDetailMemocontent.visibility = View.INVISIBLE
            // binding.llCalendarDetailNoMemo.visibility = View.VISIBLE
            binding.tvCalendarDetailMemoTitle.text = "${month}월 ${date}일의 일기"

//            binding.llCalendarDetailNoMemo.setOnClickListener {
//                // 지금 입력하기 누름
//            }

            // 차트
            // chart(memoList)

            memoListView(memoList)
        } else if (memoList?.size!! > 0 && memoContent?.size!! > 0) {
            // 둘 다 있다.
            Log.d("calendarDetail memoContent 확인...", "$memoContent")
            memoListView(memoList)

            // chart(memoList)

            if (memoContent != null) {
                memoContentView(memoContent)
            }
        }
    }

    fun memoListView(memoList: List<MemoDailyResponse.MemoItemResponse>) {
        val memoListAdapter = CalendarDetailMemoListAdapter(memoList, this@CalendarDetail)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        binding.rvCalendarDetailMemolist.adapter = memoListAdapter
        binding.rvCalendarDetailMemolist.layoutManager = layoutManager
        binding.tvCalendarDetailTitle.text = "${month}월 ${date}일 ${UserSharedPreferences.getUserNickname(this)}님의 날씨"

        // 클릭하면 수정페이지로 넘어감
        memoListAdapter.setOnItemClickListener(object : CalendarDetailMemoListAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int, memoId: Int) {
                val mIntent = Intent(this@CalendarDetail, CalendarDetailviewModify1::class.java)
                mIntent.putExtra("memoId", memoId)
                startActivity(mIntent)
            }
        })
    }

    fun memoContentView(memoContent: List<MemoDailyResponse.MemoContentResponse>) {
        val memoContentAdapter = CalendarDetailMemoContentAdapter(memoContent)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        binding.tvCalendarDetailMemoTitle.text = "${month}월 ${date}일의 일기"

        binding.rvCalendarDetailMemocontent.adapter = memoContentAdapter
        binding.rvCalendarDetailMemocontent.layoutManager = layoutManager
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun emptyView(thisDate: String?) {
        binding.llCalendarDetailNoTotalData.visibility = View.VISIBLE
        binding.tvCalendarDetailNoTotalData.visibility = View.VISIBLE

        //
        binding.tvCalendarDetailNoTotalData.setText("${month}월 ${date}일 날씨와 기록이 없어요.")

        binding.llCalendarDetailNoTotalData.setOnClickListener {
            // 입력창 이동



        }

        binding.nsCalendarDetail.visibility = View.INVISIBLE
        binding.tvCalendarDetailTitle.visibility = View.INVISIBLE
        binding.tvCalendarDetailMemoTitle.visibility = View.INVISIBLE
//        binding.llCalendarDetailRcy.visibility = View.INVISIBLE
        binding.dividerCalendarDetailMemo.visibility = View.INVISIBLE
        binding.tvCalendarDetailMemoTitle.visibility = View.INVISIBLE
        binding.rvCalendarDetailMemocontent.visibility = View.INVISIBLE
    }

//    private fun chart(memoList: List<MemoDailyResponse.MemoItemResponse>) {
//        var temprList: MutableList<TemprData> = mutableListOf()
//        val linechart = binding.chartCalendarDetail
//
//        // val linechart = findViewById<LineChart>(R.id.line_chart)
//
//        val xAxis = linechart.xAxis
//        for (i in 0 until memoList.size) {
//            temprList.add(TemprData(memoList[i].creationDatetime, memoList[i].temperature))
//        }
//
//        val entries: MutableList<Entry> = mutableListOf()
//
//        for (i in temprList.indices) {
//            entries.add(Entry(i.toFloat(), temprList[i].tempr.toFloat()))
//        }
//
//        val lineDataSet = LineDataSet(entries, "entries")
//
//        lineDataSet.apply {
//            color = resources.getColor(R.color.black, null)
//            circleRadius = 5f
//            lineWidth = 3f
//            setCircleColor(resources.getColor(R.color.gray, null))
//            circleHoleColor = resources.getColor(R.color.white, null)
//            setDrawHighlightIndicators(false)
//            // setDrawValues(true) // 숫자표시
//            // #F0A830
//            color = Color.parseColor("#F0A830")
//            valueTextColor = resources.getColor(R.color.black, null)
//            valueFormatter = DefaultValueFormatter(0) // 소숫점 자릿수 설정
//            valueTextSize = 10f
//        }
//
//        // 나머지 설정
//        linechart.apply {
//            axisRight.isEnabled = false // y축 사용여부
//            axisLeft.isEnabled = false
//            legend.isEnabled = false // legend 사용여부
//            description.isEnabled = false // 주석
//            //  isDragXEnabled = true   // x 축 드래그 여부
//            isScaleYEnabled = false // y축 줌 사용여부
//            isScaleXEnabled = false // x축 줌 사용여부
//        }
//
//        xAxis.apply {
//            isEnabled = false
//            setLabelCount(10, false)
//            setDrawGridLines(false)
//            setDrawAxisLine(true)
//            setDrawLabels(false)
//            position = XAxis.XAxisPosition.BOTTOM
//            textColor = resources.getColor(R.color.black, null)
//            textSize = 10f
//            labelRotationAngle = 0f
//            setLabelCount(10, true)
//            granularity = 95f
//        }
//        linechart.apply {
//            data = LineData(lineDataSet)
//            notifyDataSetChanged() // 데이터 갱신
//            invalidate() // view갱신
//        }
//    }
//    data class TemprData(val date: String, val tempr: Int)
}
