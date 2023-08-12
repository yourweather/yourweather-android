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
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.StatisticResponse
import com.umc.yourweather.data.service.ReportService
import com.umc.yourweather.databinding.FragmentWrittenDetailListThunderWeeklyBinding
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.presentation.adapter.WrittenRVAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WrittenDetailListFragmentThunderWeekly : Fragment() {
    private var _binding: FragmentWrittenDetailListThunderWeeklyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWrittenDetailListThunderWeeklyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataList = fetchDataFromAPI()

        binding.recyclerViewDetailThunderWeekly.layoutManager = LinearLayoutManager(requireContext())
        val adapter = WrittenRVAdapter(dataList, requireContext())
        binding.recyclerViewDetailThunderWeekly.adapter = adapter

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
        binding.tvWrittenDetailListMonthThunder.text = "$weekTitle (${getWeekText})의 번개 통계"
        iconStatisticsWeekApi(updateWeek)
    }
    private fun iconStatisticsWeekApi(ago: Int) {
        val service = RetrofitImpl.authenticatedRetrofit.create(ReportService::class.java)
        val call = service.weeklyStatistic(ago = ago)

        call.enqueue(object : Callback<BaseResponse<StatisticResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<StatisticResponse>>,
                response: Response<BaseResponse<StatisticResponse>>,
            ) {
                if (response.isSuccessful) {
                    val statisticResponse = response.body()?.result
                    if (statisticResponse != null) {
                        Log.d("${ago}주 전 Success", "${ago}주 전 디테일 lightning: ${statisticResponse.lightning}")
                        if (ago == 0) {
                            binding.tvWrittenDetailListMonthContent.text = "번개가 이번 주 날씨의 ${statisticResponse.lightning.toInt()}%를 차지했어요"
                        } else {
                            binding.tvWrittenDetailListMonthContent.text = "번개가 ${ago}주 전 날씨의 ${statisticResponse.lightning.toInt()}%를 차지했어요"
                        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToAnalysisFragment() {
        val analysisFragment = AnalysisFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fl_content, analysisFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun fetchDataFromAPI(): List<ItemWritten> {
        val dataList = mutableListOf<ItemWritten>()
        dataList.add(ItemWritten(6, 23, "월", "오전", 10, 35))
        dataList.add(ItemWritten(7, 15, "화", "오후", 2, 55))
        dataList.add(ItemWritten(7, 20, "수", "오후", 6, 10))

        return dataList
    }
}
