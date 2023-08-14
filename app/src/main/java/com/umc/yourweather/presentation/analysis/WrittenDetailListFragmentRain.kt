package com.umc.yourweather.presentation.analysis

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.yourweather.R
import com.umc.yourweather.data.entity.ItemWritten
import com.umc.yourweather.data.enums.Status
import com.umc.yourweather.data.remote.request.MemoRequest
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.SpecificMemoResponse
import com.umc.yourweather.data.remote.response.StatisticResponse
import com.umc.yourweather.data.service.ReportService
import com.umc.yourweather.databinding.FragmentWrittenDetailListRainBinding
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.presentation.adapter.WrittenRVAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WrittenDetailListFragmentRain : Fragment() {
    private var _binding: FragmentWrittenDetailListRainBinding? = null
    private val binding get() = _binding!!
    private var currentMonth = monthGenerator()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWrittenDetailListRainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rlBtn1.setOnClickListener {
            navigateToAnalysisFragment()
        }

        // 뒤로가기 버튼을 누를 때 AnalysisFragment로 이동하도록 설정
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToAnalysisFragment()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        // 인자(bundle)로부터 ago 값을 가져오기
        val ago = arguments?.getInt("ago", 0) ?: 0
        iconStatisticsMonthApi(ago)

        binding.recyclerViewDetailRain.layoutManager = LinearLayoutManager(requireContext())
    }

    // 이번 달 통계
    private fun iconStatisticsMonthApi(ago: Int) {
        val service = RetrofitImpl.authenticatedRetrofit.create(ReportService::class.java)
        val call = service.monthlyStatistic(ago = ago)

        // 로그로 확인하기 위한 언제 달인지 변수
        var viewMonth = currentMonth - ago
        binding.tvWrittenDetailListMonthRain.text = "${viewMonth}월 비 통계"

        call.enqueue(object : Callback<BaseResponse<StatisticResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<StatisticResponse>>,
                response: Response<BaseResponse<StatisticResponse>>,
            ) {
                if (response.isSuccessful) {
                    val statisticResponse = response.body()?.result
                    if (statisticResponse != null) {
                        Log.d("${ago}개월 전 ${viewMonth}월 Success", "${viewMonth}월 디테일 rainy: ${statisticResponse.rainy}")
                        binding.tvWrittenDetailListMonthContent.text = "비가 ${viewMonth}월 전체 날씨의 ${statisticResponse.rainy.toInt()}%를 차지했어요"
                        fetchSpecificMemoListByWeather(viewMonth, Status.RAINY)
                    } else {
                        Log.e("${ago}개월 전 디테일 API Error", "Response body 비었음")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("${ago}개월 전 디테일 API Error", "Response Code: ${response.code()}, Error Body: $errorBody")
                }
            }

            override fun onFailure(call: Call<BaseResponse<StatisticResponse>>, t: Throwable) {
                Log.e("${ago}개월 전 디테일 bar API Failure", "Error: ${t.message}", t)
            }
        })
    }

    // 특정 달 cloudy 리스트
    fun fetchSpecificMemoListByWeather(month: Int, weather: Status) {
        val retrofit = RetrofitImpl.authenticatedRetrofit
        val reportService = retrofit.create(ReportService::class.java)

        val call = reportService.getMonthlyReport(month, weather)
        call.enqueue(object : Callback<BaseResponse<SpecificMemoResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<SpecificMemoResponse>>,
                response: Response<BaseResponse<SpecificMemoResponse>>,
            ) {
                if (response.isSuccessful) {
                    val baseResponse = response.body()
                    val specificMemoResponse = baseResponse?.result

                    val memoList = specificMemoResponse?.memoList ?: emptyList()
                    val proportion = specificMemoResponse?.proportion

                    val memoIds = memoList.map { it.memoId }
                    val memoDateTime = memoList.map { it.dateTime }

                    Log.d("비 메모 리스트","$memoList")
                    Log.d("비 비율","$proportion")
                    Log.d("비 메모 아이디","$memoIds")
                    Log.d("비 메모 작성시간","$memoDateTime")

                } else {
                    Log.d("API call failed", "${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<BaseResponse<SpecificMemoResponse>>, t: Throwable) {
                Log.d("API call failed", "${t.message}")
            }
        })
    }

    fun monthGenerator(): Int {
        val instance = Calendar.getInstance()
        var month = (instance.get(Calendar.MONTH) + 1)
        // var week = instance.get(Calendar.WEEK_OF_MONTH).toString()
        Log.d("TimeGenerator", "Current Date: $month")

        return month
    }
    private fun navigateToAnalysisFragment() {
        val analysisFragment = AnalysisFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fl_content, analysisFragment)
            .addToBackStack(null)
            .commit()
    }

    // MemoReportResponse를 ItemWritten으로 변환하는 함수
    private fun fetchDataFromAPI(memoList: List<SpecificMemoResponse.MemoReportResponse>?): List<ItemWritten> {
        val dataList = mutableListOf<ItemWritten>()

        memoList?.forEach { memoReportResponse ->
            try {
                val dateTime = memoReportResponse.dateTime
                val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(dateTime)
                Log.d("리스트 날짜", "$date")
                val calendar = Calendar.getInstance()
                calendar.time = date

                val itemWritten = ItemWritten(
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH),
                    getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK)),
                    if (calendar.get(Calendar.AM_PM) == Calendar.AM) "오전" else "오후",
                    calendar.get(Calendar.HOUR),
                    calendar.get(Calendar.MINUTE),
                )
                dataList.add(itemWritten)
            } catch (e: ParseException) {
                Log.e("Date Parsing Error", "Error parsing date: ${memoReportResponse.dateTime}")
            }
        }

        return dataList
    }


    // Calendar.DAY_OF_WEEK 값을 요일 문자열로 변환하는 함수
    private fun getDayOfWeek(dayOfWeek: Int): String {
        return when (dayOfWeek) {
            Calendar.SUNDAY -> "일요일"
            Calendar.MONDAY -> "월요일"
            Calendar.TUESDAY -> "화요일"
            Calendar.WEDNESDAY -> "수요일"
            Calendar.THURSDAY -> "목요일"
            Calendar.FRIDAY -> "금요일"
            Calendar.SATURDAY -> "토요일"
            else -> ""
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
