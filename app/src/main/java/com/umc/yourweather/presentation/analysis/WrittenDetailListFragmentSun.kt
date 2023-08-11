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
import com.umc.yourweather.data.remote.request.MemoRequest
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.SpecificMemoResponse
import com.umc.yourweather.data.remote.response.StatisticResponse
import com.umc.yourweather.data.service.ReportService
import com.umc.yourweather.databinding.FragmentWrittenDetailListSunBinding
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.di.RetrofitImpl.authenticatedRetrofit
import com.umc.yourweather.presentation.adapter.WrittenRVAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WrittenDetailListFragmentSun : Fragment() {
    private var _binding: FragmentWrittenDetailListSunBinding? = null
    private val binding get() = _binding!!
    private var currentMonth = monthGenerator()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWrittenDetailListSunBinding.inflate(inflater, container, false)
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

        binding.recyclerViewDetailSun.layoutManager = LinearLayoutManager(requireContext())
    }

    // 이번 달 통계
    private fun iconStatisticsMonthApi(ago: Int) {
        val service = RetrofitImpl.authenticatedRetrofit.create(ReportService::class.java)
        val call = service.monthlyStatistic(ago = ago)

        // 로그로 확인하기 위한 언제 달인지 변수
        var viewMonth = currentMonth - ago
        binding.tvWrittenDetailListMonthSun.text = "${viewMonth}월 맑음 통계"

        call.enqueue(object : Callback<BaseResponse<StatisticResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<StatisticResponse>>,
                response: Response<BaseResponse<StatisticResponse>>,
            ) {
                if (response.isSuccessful) {
                    val statisticResponse = response.body()?.result // 'data'가 실제 응답 데이터를 담고 있는 필드일 경우
                    if (statisticResponse != null) {
                        Log.d("${ago}개월 전 ${viewMonth}월 Success", "${viewMonth}월 디테일 Sunny: ${statisticResponse.sunny}")
                        binding.tvWrittenDetailListMonthContent.text = "맑음이 ${viewMonth}월 전체 날씨의 ${statisticResponse.sunny.toInt()}%를 차지했어요"
                        fetchSpecificMemoListByWeather(viewMonth, MemoRequest.Status.SUNNY)
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

    // 특정 달 sunny 리스트
    fun fetchSpecificMemoListByWeather(month: Int, weather: MemoRequest.Status) {
        val retrofit = authenticatedRetrofit
        val reportService = retrofit.create(ReportService::class.java)

        val call = reportService.getMonthlyReport(month, weather)
        call.enqueue(object : Callback<BaseResponse<SpecificMemoResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<SpecificMemoResponse>>,
                response: Response<BaseResponse<SpecificMemoResponse>>,
            ) {
                if (response.isSuccessful) {
                    val memoList = response.body()?.result?.memoList
                    val memoListSize = memoList?.size ?: 0 // 리스트 개수
                    binding.tvWrittenDetailListMonthNum.text = "총 ${memoListSize}회"
                    val dataList = fetchDataFromAPI(memoList)

                    // 어댑터 초기화 및 데이터 설정
                    val adapter = WrittenRVAdapter(dataList, requireContext())
                    binding.recyclerViewDetailSun.adapter = adapter

                    memoList?.forEach { memoReportResponse ->
                        val memoId = memoReportResponse.memoId
                        val dateTime = memoReportResponse.dateTime
                        Log.d("SUNNY API", "Memo ID: $memoId, Date Time: $dateTime")
                    }
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
