package com.umc.yourweather.presentation.analysis

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.umc.yourweather.R
import com.umc.yourweather.data.entity.BarData
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.StatisticResponse
import com.umc.yourweather.data.service.ReportService
import com.umc.yourweather.databinding.FragmentBarStaticsWeeklyBinding
import com.umc.yourweather.di.RetrofitImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BarStaticsWeeklyFragment : Fragment() {
    private var _binding: FragmentBarStaticsWeeklyBinding? = null
    private val binding: FragmentBarStaticsWeeklyBinding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBarStaticsWeeklyBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
/*
        // 날씨 통계 색상 변환 함수 데이터
        val increases = mapOf(
            "맑음" to 10,
            "구름 약간" to 40,
            "비" to 0,
            "번개" to 0,
        )
        val decreases = mapOf(
            "맑음" to 0,
            "구름 약간" to 0,
            "비" to 15,
            "번개" to 20,
        )

        applyWeatherTextFormatting(increases, decreases)
*/
//        // 지난 주 데이터 리스트 생성
//        val dataList = listOf(
//            BarData("맑음", 44),
//            BarData("흐림", 60),
//            BarData("비", 40),
//            BarData("번개", 100),
//        )
//        // 이번 주 데이터 리스트 생성
//        val dataList2 = listOf(
//            BarData("맑음", 52),
//            BarData("흐림", 66),
//            BarData("비", 38),
//            BarData("번개", 90),
//        )
//
//        bindWeatherData(dataList, binding.llAnalysisBarLastWeek, ::showBallViewLastWeek)
//        bindWeatherData(dataList2, binding.llAnalysisBarThisWeek, ::showBallViewThisWeek)

        barStatisticsThisWeekApi()
        barStatisticsLastWeekApi()
        weeklyComparisonApi()
    }

    // 날씨 통계 색상 변환 함수
    private fun applyWeatherTextFormatting(increases: Map<String, Int>, decreases: Map<String, Int>) {
        val increaseColor = "#70AD47" // Increase color
        val decreaseColor = "#F7931E" // Decrease color

        val weatherToViewMap = mapOf(
            "맑음" to binding.tvAnalysisDetailStaticsSunny,
            "구름 약간" to binding.tvAnalysisDetailStaticsCloudy,
            "비" to binding.tvAnalysisDetailStaticsRainy,
            "번개" to binding.tvAnalysisDetailStaticsThunder,
        )

        for ((weather, view) in weatherToViewMap) {
            val increaseValue = increases[weather] ?: 0
            val decreaseValue = decreases[weather] ?: 0
            val actionText = if (increaseValue > 0) "증가" else if (decreaseValue > 0) "감소" else "변화가 없습니다"
            val changeValue = if (increaseValue > 0) increaseValue else decreaseValue

            val formattedText = SpannableStringBuilder()
            formattedText.append(weather)
            formattedText.append(" 비율이 ")
            val valueStart = formattedText.length
            formattedText.append("$changeValue%")
            formattedText.append(" ")

            val color = if (increaseValue > 0) increaseColor else decreaseColor
            formattedText.setSpan(
                ForegroundColorSpan(Color.parseColor(color)),
                valueStart,
                formattedText.length,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE,
            )

            formattedText.setSpan(
                ForegroundColorSpan(Color.parseColor("#000000")), // 원하는 색상으로 변경
                formattedText.length - 1,
                formattedText.length,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE,
            )

            formattedText.append("$actionText")
            if (actionText == "변화가 없습니다.") {
                formattedText.append("")
            } else {
                formattedText.append("했습니다.")
            }

            view.text = formattedText
        }
    }

    private fun bindWeatherData(dataList: List<BarData>, layout: LinearLayout, clickListener: (String) -> Unit) {
        // 각 데이터 값에 해당하는 너비 계산
        val sum = dataList.sumOf { it.value }

        for (data in dataList) {
            val value = data.value
            val ratio = if (sum != 0) value / sum else 0.0
            val width: Float = (ratio.toFloat() * 100)

            // 새로운 View 생성
            val view = View(requireContext())

            // View의 레이아웃 파라미터 설정
            view.layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.MATCH_PARENT,
                width,
            )

            // 배경 파일 적용
            val drawableRes = getDrawableResForWeather(data.label)
            view.background = ContextCompat.getDrawable(requireContext(), drawableRes)

            // 레이아웃에 View 추가
            layout.addView(view)

            // 클릭 리스너 설정
            view.setOnClickListener {
                clickListener(data.label)
            }
        }
    }

    // 각 날씨별 배경 파일 지정
    private fun getDrawableResForWeather(weatherLabel: String): Int {
        return when (weatherLabel) {
            "맑음" -> R.drawable.bg_yellow_rec_round_sun
            "흐림" -> R.drawable.bg_gray_rec_cloud
            "비" -> R.drawable.bg_blue_rec_rain
            "번개" -> R.drawable.bg_darkblue_rec_round_thunder
            else -> R.drawable.bg_darkblue_rec_round_thunder
        }
    }

    // 날씨별 말풍선 - 지난 주
    private fun showBallViewLastWeek(weatherLabel: String) {
        // 모든 텍스트뷰 숨기기
        binding.tvBalloonSunLastWeek.visibility = View.GONE
        binding.tvBalloonRainLastWeek.visibility = View.GONE
        binding.tvBalloonCloudLastWeek.visibility = View.GONE
        binding.tvBalloonThunderLastWeek.visibility = View.GONE

        // 선택한 날씨에 맞는 TextView를 보이도록 설정
        when (weatherLabel) {
            "맑음" -> {
                binding.tvBalloonSunLastWeek.visibility = View.VISIBLE
                hideBallViewAfterDelay(binding.tvBalloonSunLastWeek)
            }
            "흐림" -> {
                binding.tvBalloonCloudLastWeek.visibility = View.VISIBLE
                hideBallViewAfterDelay(binding.tvBalloonCloudLastWeek)
            }
            "번개" -> {
                binding.tvBalloonThunderLastWeek.visibility = View.VISIBLE
                hideBallViewAfterDelay(binding.tvBalloonThunderLastWeek)
            }
            "비" -> {
                binding.tvBalloonRainLastWeek.visibility = View.VISIBLE
                hideBallViewAfterDelay(binding.tvBalloonRainLastWeek)
            }
        }
    }

    // 날씨별 말풍선 - 이번 주
    private fun showBallViewThisWeek(weatherLabel: String) {
        // 모든 텍스트뷰 숨기기
        binding.tvBalloonSunThisWeek.visibility = View.GONE
        binding.tvBalloonRainThisWeek.visibility = View.GONE
        binding.tvBalloonCloudThisWeek.visibility = View.GONE
        binding.tvBalloonThunderThisWeek.visibility = View.GONE

        // 선택한 날씨에 맞는 TextView를 보이도록 설정
        when (weatherLabel) {
            "맑음" -> {
                binding.tvBalloonSunThisWeek.visibility = View.VISIBLE
                hideBallViewAfterDelay(binding.tvBalloonSunThisWeek)
            }
            "흐림" -> {
                binding.tvBalloonCloudThisWeek.visibility = View.VISIBLE
                hideBallViewAfterDelay(binding.tvBalloonCloudThisWeek)
            }
            "번개" -> {
                binding.tvBalloonThunderThisWeek.visibility = View.VISIBLE
                hideBallViewAfterDelay(binding.tvBalloonThunderThisWeek)
            }
            "비" -> {
                binding.tvBalloonRainThisWeek.visibility = View.VISIBLE
                hideBallViewAfterDelay(binding.tvBalloonRainThisWeek)
            }
        }
    }

    // 일정 시간 지난 후 말풍선 숨기기
    private fun hideBallViewAfterDelay(view: View) {
        val handler = Handler()
        handler.postDelayed({
            view.visibility = View.GONE
        }, 1500) // 1.5초 후에 말풍선을 숨김
    }

    // 이번 주 bar 통계
    private fun barStatisticsThisWeekApi() {
        val service = RetrofitImpl.authenticatedRetrofit.create(ReportService::class.java)
        val call = service.weeklyStatistic(ago = 0) // 'ago'에 적절한 값을 설정해주세요

        call.enqueue(object : Callback<BaseResponse<StatisticResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<StatisticResponse>>,
                response: Response<BaseResponse<StatisticResponse>>,
            ) {
                if (response.isSuccessful) {
                    val statisticResponse = response.body()?.result // 'data'가 실제 응답 데이터를 담고 있는 필드일 경우
                    if (statisticResponse != null) {
                        Log.d("이번 주 bar API Success", "Sunny: ${statisticResponse.sunny}, Cloudy: ${statisticResponse.cloudy}, Rainy: ${statisticResponse.rainy}, Lightning: ${statisticResponse.lightning}")

                        // 데이터 리스트 생성
                        val dataList = listOf(
                            BarData("맑음", statisticResponse.sunny.toInt()),
                            BarData("구름 약간", statisticResponse.cloudy.toInt()),
                            BarData("비", statisticResponse.rainy.toInt()),
                            BarData("번개", statisticResponse.lightning.toInt()),
                        )

                        // 데이터 표시 함수 호출
                        bindWeatherData(dataList, binding.llAnalysisBarThisWeek, ::showBallViewThisWeek)
                    } else {
                        Log.e("이번 주 bar API Error", "Response body 비었음")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("이번 주 bar API Error", "Response Code: ${response.code()}, Error Body: $errorBody")
                }
            }

            override fun onFailure(call: Call<BaseResponse<StatisticResponse>>, t: Throwable) {
                Log.e("이번 주 bar API Failure", "Error: ${t.message}", t)
            }
        })
    }

    // 지난 주 bar 통계
    private fun barStatisticsLastWeekApi() {
        val service = RetrofitImpl.authenticatedRetrofit.create(ReportService::class.java)
        val call = service.weeklyStatistic(ago = 1) // '지난 주

        call.enqueue(object : Callback<BaseResponse<StatisticResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<StatisticResponse>>,
                response: Response<BaseResponse<StatisticResponse>>,
            ) {
                if (response.isSuccessful) {
                    val statisticResponse = response.body()?.result // 'data'가 실제 응답 데이터를 담고 있는 필드일 경우
                    if (statisticResponse != null) {
                        Log.d("지난 주 bar API Success", "Sunny: ${statisticResponse.sunny}, Cloudy: ${statisticResponse.cloudy}, Rainy: ${statisticResponse.rainy}, Lightning: ${statisticResponse.lightning}")

                        // 데이터 리스트 생성
                        val dataList = listOf(
                            BarData("맑음", statisticResponse.sunny.toInt()),
                            BarData("구름 약간", statisticResponse.cloudy.toInt()),
                            BarData("비", statisticResponse.rainy.toInt()),
                            BarData("번개", statisticResponse.lightning.toInt()),
                        )

                        // 데이터 표시 함수 호출
                        bindWeatherData(dataList, binding.llAnalysisBarLastWeek, ::showBallViewLastWeek)
                    } else {
                        Log.e("지난 주 bar API Error", "Response body 비었음")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("지난 주 bar API Error", "Response Code: ${response.code()}, Error Body: $errorBody")
                }
            }

            override fun onFailure(call: Call<BaseResponse<StatisticResponse>>, t: Throwable) {
                Log.e("지난 주 bar API Failure", "Error: ${t.message}", t)
            }
        })
    }

    // 주간 데이터 비교
    private fun weeklyComparisonApi() {
        val service = RetrofitImpl.authenticatedRetrofit.create(ReportService::class.java)
        val call = service.weeklyComparison(ago = 1)

        call.enqueue(object : Callback<BaseResponse<StatisticResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<StatisticResponse>>,
                response: Response<BaseResponse<StatisticResponse>>,
            ) {
                if (response.isSuccessful) {
                    val statisticResponse = response.body()?.result // 'data'가 실제 응답 데이터를 담고 있는 필드일 경우
                    if (statisticResponse != null) {
                        Log.d("주간 데이터 비교 API Success", "Sunny: ${statisticResponse.sunny}, Cloudy: ${statisticResponse.cloudy}, Rainy: ${statisticResponse.rainy}, Lightning: ${statisticResponse.lightning}")

                        val increases = mutableMapOf<String, Int>()
                        val decreases = mutableMapOf<String, Int>()

                        if (statisticResponse.sunny >= 0) increases["맑음"] = statisticResponse.sunny.toInt() else decreases["맑음"] = 0
                        if (statisticResponse.cloudy >= 0) increases["구름 약간"] = statisticResponse.cloudy.toInt() else decreases["구름 약간"] = 0
                        if (statisticResponse.rainy >= 0) increases["비"] = statisticResponse.rainy.toInt() else decreases["비"] = 15
                        if (statisticResponse.lightning >= 0) increases["번개"] = statisticResponse.lightning.toInt() else decreases["번개"] = 20

                        applyWeatherTextFormatting(increases, decreases)
                    } else {
                        Log.e("주간 데이터 비교 bar API Error", "Response body 비었음")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("주간 데이터 비교 bar API Error", "Response Code: ${response.code()}, Error Body: $errorBody")
                }
            }

            override fun onFailure(call: Call<BaseResponse<StatisticResponse>>, t: Throwable) {
                Log.e("주간 데이터 비교 bar API Failure", "Error: ${t.message}", t)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
