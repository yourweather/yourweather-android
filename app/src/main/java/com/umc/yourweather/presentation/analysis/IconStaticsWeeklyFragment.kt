package com.umc.yourweather.presentation.analysis

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.yourweather.R
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.StatisticResponse
import com.umc.yourweather.data.service.ReportService
import com.umc.yourweather.databinding.FragmentIconStaticsWeeklyBinding
import com.umc.yourweather.di.RetrofitImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IconStaticsWeeklyFragment : Fragment() {
    private var _binding: FragmentIconStaticsWeeklyBinding? = null
    private val binding: FragmentIconStaticsWeeklyBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentIconStaticsWeeklyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 초기화 때 이번 달의 통계를 가져오기 위해 ago 값을 설정
        val initialAgo = 0
        StatisticsWeekApi(initialAgo)
        Log.d("${initialAgo}전으로", "$initialAgo")

        setupOnClickListeners()
    }

    private fun setupOnClickListeners() {

        // 현재 주
        binding.tvUnwrittenTitleWeekly.text = "이번 주"

        // 오른쪽 버튼 투명도 0.5로 고정
        binding.btnStaticsRightDateWeekly.alpha = 0.5f

        var updateWeek = 0 // 현재 주
        // 과거 주로 이동
        binding.btnStaticsLeftDateWeekly.setOnClickListener {
            if (updateWeek < 4) {
                updateWeek++
                updateTitleAndFetchStatistics(updateWeek)
                binding.btnStaticsRightDateWeekly.alpha = 1f
            }
            if (updateWeek == 4) {
                binding.btnStaticsLeftDateWeekly.alpha = 0.5f
                binding.btnStaticsRightDateWeekly.alpha = 1f
            }
        }

        // 현재 주로 오기
        binding.btnStaticsRightDateWeekly.setOnClickListener {
            if (updateWeek > 0) {
                updateWeek--
                updateTitleAndFetchStatistics(updateWeek)
            }
            if (updateWeek == 0) {
                binding.btnStaticsRightDateWeekly.alpha = 0.5f
                binding.btnStaticsLeftDateWeekly.alpha = 1f
            }
        }
        // sun 상세 리스트 넘어가기
        binding.llSunWeekly.setOnClickListener {
            val fragment = WrittenDetailListFragmentSunWeekly()

            // ago 값을 인자로 전달
            val args = Bundle()
            args.putInt("updateWeek", updateWeek)
            fragment.arguments = args

            replaceFragment(fragment)
        }
        binding.btnStaticsRightDetail1Weekly.setOnClickListener {
            val fragment = WrittenDetailListFragmentSunWeekly()

            // ago 값을 인자로 전달
            val args = Bundle()
            args.putInt("updateWeek", updateWeek)
            fragment.arguments = args

            replaceFragment(fragment)        }

        // cloud 상세 리스트 넘어가기
        binding.llCloudWeekly.setOnClickListener {
            val fragment = WrittenDetailListFragmentCloudWeekly()

            // ago 값을 인자로 전달
            val args = Bundle()
            args.putInt("updateWeek", updateWeek)
            fragment.arguments = args

            replaceFragment(fragment)
        }
        binding.btnStaticsRightDetail2Weekly.setOnClickListener {
            val fragment = WrittenDetailListFragmentCloudWeekly()

            // ago 값을 인자로 전달
            val args = Bundle()
            args.putInt("updateWeek", updateWeek)
            fragment.arguments = args

            replaceFragment(fragment)
        }

        // rain 상세 리스트 넘어가기
        binding.llRainWeekly.setOnClickListener {
            val fragment = WrittenDetailListFragmentRainWeekly()

            // ago 값을 인자로 전달
            val args = Bundle()
            args.putInt("updateWeek", updateWeek)
            fragment.arguments = args

            replaceFragment(fragment)
        }
        binding.btnStaticsRightDetail3Weekly.setOnClickListener {
            val fragment = WrittenDetailListFragmentRainWeekly()

            // ago 값을 인자로 전달
            val args = Bundle()
            args.putInt("updateWeek", updateWeek)
            fragment.arguments = args

            replaceFragment(fragment)
        }

        // thunder 상세 리스트 넘어가기
        binding.llThunderWeekly.setOnClickListener {
            val fragment = WrittenDetailListFragmentThunderWeekly()

            // ago 값을 인자로 전달
            val args = Bundle()
            args.putInt("updateWeek", updateWeek)
            fragment.arguments = args

            replaceFragment(fragment)
        }
        binding.btnStaticsRightDetail4Weekly.setOnClickListener {
            val fragment = WrittenDetailListFragmentThunderWeekly()

            // ago 값을 인자로 전달
            val args = Bundle()
            args.putInt("updateWeek", updateWeek)
            fragment.arguments = args

            replaceFragment(fragment)
        }
    }
    private fun StatisticsWeekApi(ago: Int) {
        val service = RetrofitImpl.authenticatedRetrofit.create(ReportService::class.java)
        val call = service.weeklyStatistic(ago = ago) // 이번 달

        call.enqueue(object : Callback<BaseResponse<StatisticResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<StatisticResponse>>,
                response: Response<BaseResponse<StatisticResponse>>,
            ) {
                if (response.isSuccessful) {
                    val statisticResponse = response.body()?.result // 'data'가 실제 응답 데이터를 담고 있는 필드일 경우
                    if (statisticResponse != null) {
                        Log.d("${ago}주 전 디테일 Success", "${ago}주 전 디테일 Sunny: ${statisticResponse.sunny}, Cloudy: ${statisticResponse.cloudy}, Rainy: ${statisticResponse.rainy}, Lightning: ${statisticResponse.lightning}")
                        binding.tvStaticIconDetailSunnyWeekly.text = "${statisticResponse.sunny.toInt()}%"
                        binding.tvStaticIconDetailCloudyWeekly.text = "${statisticResponse.cloudy.toInt()}%"
                        binding.tvStaticIconDetailRainyWeekly.text = "${statisticResponse.rainy.toInt()}%"
                        binding.tvStaticIconDetailThunderWeekly.text = "${statisticResponse.lightning.toInt()}%"
                    } else {
                        Log.e("${ago}주 전 디테일 API Error", "Response body 비었음")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("${ago}주 전 디테일 API Error", "Response Code: ${response.code()}, Error Body: $errorBody")
                }
            }

            override fun onFailure(call: Call<BaseResponse<StatisticResponse>>, t: Throwable) {
                Log.e("${ago}주 전 디테일 bar API Failure", "Error: ${t.message}", t)
            }
        })
    }
    private fun updateTitleAndFetchStatistics(weekAgo: Int) {
        val ago = weekAgo
        binding.tvUnwrittenTitleWeekly.text = "${ago}주 전"
        StatisticsWeekApi(ago)

        if (ago == 0) {
            binding.tvUnwrittenTitleWeekly.text = "이번 주"
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fl_content, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
