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
import com.umc.yourweather.databinding.FragmentWrittenDetailListSunBinding
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.presentation.adapter.WrittenRVAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

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

        val dataList = fetchDataFromAPI()

        binding.recyclerViewDetailSun.layoutManager = LinearLayoutManager(requireContext())
        val adapter = WrittenRVAdapter(dataList, requireContext())
        binding.recyclerViewDetailSun.adapter = adapter

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
    }
    // 이번 달 통계
    private fun iconStatisticsMonthApi(ago: Int) {
        val service = RetrofitImpl.authenticatedRetrofit.create(ReportService::class.java)
        val call = service.monthlyStatistic(ago = ago)

        // 로그로 확인하기 위한 언제 달인지 변수
        var viewMonth = currentMonth - ago
        binding.tvWrittenDetailListMonthSun.text = "${viewMonth}월 맑음"

        call.enqueue(object : Callback<BaseResponse<StatisticResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<StatisticResponse>>,
                response: Response<BaseResponse<StatisticResponse>>,
            ) {
                if (response.isSuccessful) {
                    val statisticResponse = response.body()?.result // 'data'가 실제 응답 데이터를 담고 있는 필드일 경우
                    if (statisticResponse != null) {
                        Log.d("${ago}개월 전 ${viewMonth}월 Success", "${viewMonth}월 디테일 Sunny: ${statisticResponse.sunny}")
                        binding.tvWrittenDetailListMonthContent.text = "이다은 님의 ${viewMonth}월 중 ${statisticResponse.sunny.toInt()}%가 맑은 날씨였어요"

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

    private fun fetchDataFromAPI(): List<ItemWritten> {
        val dataList = mutableListOf<ItemWritten>()
        dataList.add(ItemWritten(8, 1, "화", "오전", 6, 10))
        dataList.add(ItemWritten(8, 1, "화", "오전", 10, 9))
        dataList.add(ItemWritten(8, 1, "화", "오후", 6, 30))
        dataList.add(ItemWritten(8, 2, "수", "오전", 8, 10))
        dataList.add(ItemWritten(8, 3, "목", "오전", 11, 42))

        return dataList
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
