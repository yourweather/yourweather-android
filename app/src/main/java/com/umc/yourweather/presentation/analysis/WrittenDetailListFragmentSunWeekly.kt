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
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.SpecificMemoResponse
import com.umc.yourweather.data.remote.response.StatisticResponse
import com.umc.yourweather.data.service.ReportService
import com.umc.yourweather.databinding.FragmentWrittenDetailListSunWeeklyBinding
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.presentation.adapter.WrittenRVAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WrittenDetailListFragmentSunWeekly : Fragment() {
    private var _binding: FragmentWrittenDetailListSunWeeklyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWrittenDetailListSunWeeklyBinding.inflate(inflater, container, false)
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
        val updateWeek = arguments?.getInt("updateWeek", 0) ?: 0

        iconStatisticsWeekApi(updateWeek)

        binding.recyclerViewDetailWeeklySun.layoutManager = LinearLayoutManager(requireContext())
    }

    // 이번 주 통계 updateWeek == ago
    private fun iconStatisticsWeekApi(updateWeek: Int) {
        val service = RetrofitImpl.authenticatedRetrofit.create(ReportService::class.java)
        val call = service.weeklyStatistic(ago = updateWeek)

        call.enqueue(object : Callback<BaseResponse<StatisticResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<StatisticResponse>>,
                response: Response<BaseResponse<StatisticResponse>>,
            ) {
                if (response.isSuccessful) {
                    val statisticResponse = response.body()?.result
                    if (statisticResponse != null) {
                        Log.d("${updateWeek}주 전 Success", "${updateWeek}주 전 디테일 Sunny: ${statisticResponse.sunny}")
                        // 정확한 주 숫자
                        val getWeekText = getWeekText(updateWeek)
                        // updateWeek에 따른 주차 텍스트 분기문
                        val weekTitle = when (updateWeek) {
                            0 -> "이번 주"
                            1 -> "1주 전"
                            2 -> "2주 전"
                            3 -> "3주 전"
                            else -> "$updateWeek 주 전" // 4주 이상 전의 경우
                        }

                        binding.tvWrittenDetailListMonthSun.text = "$weekTitle ($getWeekText)의 맑음 통계"
                        binding.tvWrittenDetailListMonthContent.text = "맑음이 $weekTitle 날씨의 ${statisticResponse.sunny.toInt()}%를 차지했어요"

                        fetchSpecificMemoListByWeather(updateWeek, Status.SUNNY)

                    } else {
                        Log.e("${updateWeek}개월 전 디테일 API Error", "Response body 비었음")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("${updateWeek}개월 전 디테일 API Error", "Response Code: ${response.code()}, Error Body: $errorBody")
                }
            }

            override fun onFailure(call: Call<BaseResponse<StatisticResponse>>, t: Throwable) {
                Log.e("${updateWeek}개월 전 디테일 bar API Failure", "Error: ${t.message}", t)
            }
        })
    }

    // 특정 주 sunny 리스트
    fun fetchSpecificMemoListByWeather(week: Int, weather: Status) {
        val retrofit = RetrofitImpl.authenticatedRetrofit
        val reportService = retrofit.create(ReportService::class.java)

        val call = reportService.getWeeklyReport(week, weather)
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

                    val memoIds = memoList.map { it.memoId.toInt() }
                    val memoDateTime = memoList.map { it.dateTime }

                    Log.d("$weather 메모 리스트", "$memoList")
                    Log.d("$weather 비율", "$proportion")
                    Log.d("$weather 메모 아이디", "$memoIds")
                    Log.d("$weather 메모 작성시간", "$memoDateTime")

                    val formattedMemoList = memoList.map { memoReportResponse ->
                        val formattedDateTime = formatDate(memoReportResponse.dateTime)
                        ItemWritten(memoReportResponse.memoId, memoReportResponse.dateTime, formattedDateTime)
                    }

                    // 어댑터에 변환된 데이터를 전달하여 연결
                    binding.recyclerViewDetailWeeklySun.layoutManager = LinearLayoutManager(requireContext())
                    val adapter = context?.let { WrittenRVAdapter(formattedMemoList, it, memoIds) }
                    binding.recyclerViewDetailWeeklySun.adapter = adapter

                    binding.tvWrittenDetailListMonthNum.text = "총 ${memoIds.size}회"
                } else {
                    Log.d("API call failed", "${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<BaseResponse<SpecificMemoResponse>>, t: Throwable) {
                Log.d("API call failed", "${t.message}")
            }
        })
    }
    private fun formatDate(dateTime: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("M월 d일 E요일 a h:mm", Locale.getDefault())

        val date = inputFormat.parse(dateTime)
        return outputFormat.format(date)
    }
    // 정확한 주 숫자
    private fun getWeekText(updateWeek: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.WEEK_OF_YEAR, -updateWeek)

        val dateFormat = SimpleDateFormat("M.d", Locale.getDefault())

        // Find the first day of the specified previous week (Monday)
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DAY_OF_WEEK, -1)
        }
        val previousWeekStart = dateFormat.format(calendar.time)

        // Find the last day of the specified previous week (Sunday)
        calendar.add(Calendar.DAY_OF_WEEK, 6)
        val previousWeekEnd = dateFormat.format(calendar.time)

        return "$previousWeekStart ~ $previousWeekEnd"
    }

    private fun navigateToAnalysisFragment() {
        val analysisFragment = AnalysisFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fl_content, analysisFragment)
            .addToBackStack(null)
            .commit()
    }

//    // MemoReportResponse를 ItemWritten으로 변환하는 함수
//    private fun fetchDataFromAPI(memoList: List<SpecificMemoResponse.MemoReportResponse>?): List<ItemWritten> {
//        val dataList = mutableListOf<ItemWritten>()
//
//        memoList?.forEach { memoReportResponse ->
//            val dateTime = memoReportResponse.dateTime
//            val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(dateTime)
//            Log.d("리스트 날짜", "$date")
//            val calendar = Calendar.getInstance()
//            calendar.time = date
//
//            val itemWritten = ItemWritten(
//                calendar.get(Calendar.MONTH) + 1,
//                calendar.get(Calendar.DAY_OF_MONTH),
//                getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK)),
//                if (calendar.get(Calendar.AM_PM) == Calendar.AM) "오전" else "오후",
//                calendar.get(Calendar.HOUR),
//                calendar.get(Calendar.MINUTE),
//            )
//            dataList.add(itemWritten)
//        }
//
//        return dataList
//    }

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
