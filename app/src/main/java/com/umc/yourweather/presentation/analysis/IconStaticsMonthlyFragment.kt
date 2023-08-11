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
import com.umc.yourweather.databinding.FragmentIconStaticsMonthlyBinding
import com.umc.yourweather.di.RetrofitImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class IconStaticsMonthlyFragment : Fragment() {
    private var _binding: FragmentIconStaticsMonthlyBinding? = null
    private val binding: FragmentIconStaticsMonthlyBinding get() = _binding!!
    private var currentMonth = monthGenerator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentIconStaticsMonthlyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 버튼 클릭 시 화면 전환 함수
        setupOnClickListeners()

        // 각 월별 통계 함수
        barStatisticsThisMonthApi()
    }

    // 이번 달 통계
    private fun barStatisticsThisMonthApi() {
        val service = RetrofitImpl.authenticatedRetrofit.create(ReportService::class.java)
        val call = service.monthlyStatistic(ago = 0) // 이번 달

        call.enqueue(object : Callback<BaseResponse<StatisticResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<StatisticResponse>>,
                response: Response<BaseResponse<StatisticResponse>>,
            ) {
                if (response.isSuccessful) {
                    val statisticResponse = response.body()?.result // 'data'가 실제 응답 데이터를 담고 있는 필드일 경우
                    if (statisticResponse != null) {
                        Log.d("월별 디테일 API Success", "월별 디테일 Sunny: ${statisticResponse.sunny}, Cloudy: ${statisticResponse.cloudy}, Rainy: ${statisticResponse.rainy}, Lightning: ${statisticResponse.lightning}")
                    } else {
                        Log.e("월별 디테일 API Error", "Response body 비었음")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("월별 디테일 API Error", "Response Code: ${response.code()}, Error Body: $errorBody")
                }
            }

            override fun onFailure(call: Call<BaseResponse<StatisticResponse>>, t: Throwable) {
                Log.e("월별 디테일 bar API Failure", "Error: ${t.message}", t)
            }
        })
    }

    private fun setupOnClickListeners() {
        // sun 상세 리스트 넘어가기
        binding.llSunMonthly.setOnClickListener {
            replaceFragment(WrittenDetailListFragmentSun())
        }
        binding.btnStaticsRightDetail1Monthly.setOnClickListener {
            replaceFragment(WrittenDetailListFragmentSun())
        }

        // cloud 상세 리스트 넘어가기
        binding.llCloudMonthly.setOnClickListener {
            replaceFragment(WrittenDetailListFragmentCloud())
        }
        binding.btnStaticsRightDetail2Monthly.setOnClickListener {
            replaceFragment(WrittenDetailListFragmentSun())
        }

        // rain 상세 리스트 넘어가기
        binding.llRainMonthly.setOnClickListener {
            replaceFragment(WrittenDetailListFragmentRain())
        }
        binding.btnStaticsRightDetail3Monthly.setOnClickListener {
            replaceFragment(WrittenDetailListFragmentSun())
        }

        // thunder 상세 리스트 넘어가기
        binding.llThunderMonthly.setOnClickListener {
            replaceFragment(WrittenDetailListFragmentThunder())
        }
        binding.btnStaticsRightDetail4Monthly.setOnClickListener {
            replaceFragment(WrittenDetailListFragmentSun())
        }

        // 현재 달
        binding.tvUnwrittenTitleMonthly.text = currentMonth.toString() + "월"

        // 오른쪽 버튼 투명도 0.5로 고정
        binding.btnStaticsRightDateMonthly.alpha = 0.5f

        var updateMonth = currentMonth
        // 과거 달로 이동
        binding.btnStaticsLeftDateMonthly.setOnClickListener {
            if (updateMonth > 1) {
                updateMonth--
                binding.tvUnwrittenTitleMonthly.text = updateMonth.toString() + "월"
                binding.btnStaticsRightDateMonthly.alpha = 1f
            }
            if (updateMonth == 1) {
                binding.btnStaticsLeftDateMonthly.alpha = 0.5f
                binding.btnStaticsRightDateMonthly.alpha = 1f
            }
        }

        // 현재 달로 오기
        binding.btnStaticsRightDateMonthly.setOnClickListener {
            if (updateMonth < currentMonth) {
                updateMonth++
                binding.tvUnwrittenTitleMonthly.text = updateMonth.toString() + "월"
                binding.btnStaticsRightDateMonthly.alpha = 1f
            }
            if (updateMonth == currentMonth) {
                binding.btnStaticsRightDateMonthly.alpha = 0.5f
                binding.btnStaticsLeftDateMonthly.alpha = 1f
            }
        }
    }

    fun monthGenerator(): Int {
        val instance = Calendar.getInstance()
        var month = (instance.get(Calendar.MONTH) + 1)
        // var week = instance.get(Calendar.WEEK_OF_MONTH).toString()
        Log.d("TimeGenerator", "Current Date: $month")

        return month
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
