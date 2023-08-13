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
import com.umc.yourweather.databinding.FragmentBarStaticsMonthlyBinding
import com.umc.yourweather.di.RetrofitImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BarStaticsMonthlyFragment : Fragment() {
    private var _binding: FragmentBarStaticsMonthlyBinding? = null
    private val binding: FragmentBarStaticsMonthlyBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBarStaticsMonthlyBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val increases = mapOf(
//            "맑음" to 10,
//            "구름 약간" to 40,
//            "비" to 0,
//            "번개" to 0,
//        )
//        val decreases = mapOf(
//            "맑음" to 0,
//            "구름 약간" to 0,
//            "비" to 15,
//            "번개" to 20,
//        )
//
//        applyWeatherTextFormatting(increases, decreases)

//        val dataList = listOf(
//            BarData("맑음", 44),
//            BarData("흐림", 29),
//            BarData("비", 24),
//            BarData("번개", 59),
//        )
//
//        val dataList2 = listOf(
//            BarData("맑음", 48),
//            BarData("흐림", 42),
//            BarData("비", 20),
//            BarData("번개", 37),
//        )
//
//        bindWeatherData(dataList, binding.llAnalysisBarLastMonth, ::showBallViewLastMonth)
//        bindWeatherData(dataList2, binding.llAnalysisBarThisMonth, ::showBallViewThisMonth)

        CoroutineScope(Dispatchers.Main).launch {
            // 지난 달, 이번 달 정확한 달 숫자
            val previousMonth = getPreviousMonth()
            val currentMonth = getCurrentMonth()
            binding.tvAnalysisMonthNum.text = previousMonth
            binding.tvAnalysisMonthThisNum.text = currentMonth

            barStatisticsThisMonthApi()
            barStatisticsLastMonthApi()
            weeklyComparisonApi()
        }
    }

    // 지난 달 숫자
    private fun getPreviousMonth(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1) // Subtract 1 month to get the previous month

        val dateFormat = SimpleDateFormat("M월", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    // 이번 달 숫자
    private fun getCurrentMonth(): String {
        val calendar = Calendar.getInstance()

        val dateFormat = SimpleDateFormat("M월", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun applyWeatherTextFormatting(increases: Map<String, Int>, decreases: Map<String, Int>) {
        val increaseColor = "#70AD47" // Increase color
        val decreaseColor = "#F7931E" // Decrease color

        val weatherToViewMap = mapOf(
            "맑음" to Pair(binding.tvAnalysisDetailStaticsSunny, binding.ivBarStaticsArrow1),
            "구름 약간" to Pair(binding.tvAnalysisDetailStaticsCloudy, binding.ivBarStaticsArrow2),
            "비" to Pair(binding.tvAnalysisDetailStaticsRainy, binding.ivBarStaticsArrow3),
            "번개" to Pair(binding.tvAnalysisDetailStaticsThunder, binding.ivBarStaticsArrow4),
        )

        for ((weather, viewPair) in weatherToViewMap) {
            val (textView, imageView) = viewPair
            val increaseValue = increases[weather] ?: 0
            val decreaseValue = decreases[weather] ?: 0

            // Update text view
            val formattedText = if (increaseValue == 0 && decreaseValue == 0) {
                "$weather 비율이 동일합니다."
            } else {
                val actionText = if (increaseValue > 0) "증가" else if (decreaseValue > 0) "감소" else "변화가 없습니다"
                val changeValue = if (increaseValue > 0) increaseValue else decreaseValue

                val color = if (increaseValue > 0) increaseColor else decreaseColor

                val spannableText = SpannableStringBuilder()
                    .append("$weather 비율이 ")
                    .apply {
                        append("$changeValue%")
                        setSpan(
                            ForegroundColorSpan(Color.parseColor(color)),
                            length - changeValue.toString().length - 1,
                            length - 1,
                            Spannable.SPAN_EXCLUSIVE_INCLUSIVE,
                        )
                    }
                    .append(" $actionText")
                    .apply {
                        if (actionText != "변화가 없습니다.") {
                            append("했습니다.")
                        }
                    }
                spannableText
            }

            textView.text = formattedText

            // Update image view
            val arrowDrawableId = when {
                increaseValue > 0 -> R.drawable.ic_uparrow
                decreaseValue > 0 -> R.drawable.ic_downarrow
                else -> R.drawable.ic_certain
            }
            imageView.setImageResource(arrowDrawableId)
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

    private fun getDrawableResForWeather(weatherLabel: String): Int {
        return when (weatherLabel) {
            "맑음" -> R.drawable.bg_yellow_rec_round_sun
            "흐림" -> R.drawable.bg_gray_rec_cloud
            "비" -> R.drawable.bg_blue_rec_rain
            "번개" -> R.drawable.bg_darkblue_rec_round_thunder
            else -> R.drawable.bg_darkblue_rec_round_thunder
        }
    }
    private fun showBallViewLastMonth(weatherLabel: String) {
        // 모든 텍스트뷰 숨기기
        binding.tvBalloonSun.visibility = View.GONE
        binding.tvBalloonRain.visibility = View.GONE
        binding.tvBalloonCloud.visibility = View.GONE
        binding.tvBalloonThunder.visibility = View.GONE

        // 선택한 날씨에 맞는 TextView를 보이도록 설정
        when (weatherLabel) {
            "맑음" -> {
                binding.tvBalloonSun.visibility = View.VISIBLE
                hideBallViewAfterDelay(binding.tvBalloonSun)
            }
            "흐림" -> {
                binding.tvBalloonCloud.visibility = View.VISIBLE
                hideBallViewAfterDelay(binding.tvBalloonCloud)
            }
            "번개" -> {
                binding.tvBalloonThunder.visibility = View.VISIBLE
                hideBallViewAfterDelay(binding.tvBalloonThunder)
            }
            "비" -> {
                binding.tvBalloonRain.visibility = View.VISIBLE
                hideBallViewAfterDelay(binding.tvBalloonRain)
            }
        }
    }

    private fun showBallViewThisMonth(weatherLabel: String) {
        // 모든 텍스트뷰 숨기기
        binding.tvBalloonSunThis.visibility = View.GONE
        binding.tvBalloonRainThis.visibility = View.GONE
        binding.tvBalloonCloudThis.visibility = View.GONE
        binding.tvBalloonThunderThis.visibility = View.GONE

        // 선택한 날씨에 맞는 TextView를 보이도록 설정
        when (weatherLabel) {
            "맑음" -> {
                binding.tvBalloonSunThis.visibility = View.VISIBLE
                hideBallViewAfterDelay(binding.tvBalloonSunThis)
            }
            "흐림" -> {
                binding.tvBalloonCloudThis.visibility = View.VISIBLE
                hideBallViewAfterDelay(binding.tvBalloonCloudThis)
            }
            "번개" -> {
                binding.tvBalloonThunderThis.visibility = View.VISIBLE
                hideBallViewAfterDelay(binding.tvBalloonThunderThis)
            }
            "비" -> {
                binding.tvBalloonRainThis.visibility = View.VISIBLE
                hideBallViewAfterDelay(binding.tvBalloonRainThis)
            }
        }
    }
    private fun hideBallViewAfterDelay(view: View) {
        val handler = Handler()
        handler.postDelayed({
            view.visibility = View.GONE
        }, 1500) // 1.5초 후에 말풍선을 숨김
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
                    val statisticResponse = response.body()?.result //
                    if (statisticResponse != null) {
                        Log.d("이번 달 bar API Success", "이번 달 Sunny: ${statisticResponse.sunny}, Cloudy: ${statisticResponse.cloudy}, Rainy: ${statisticResponse.rainy}, Lightning: ${statisticResponse.lightning}")

                        // 데이터 리스트 생성
                        val dataList = listOf(
                            BarData("맑음", statisticResponse.sunny.toInt()),
                            BarData("구름 약간", statisticResponse.cloudy.toInt()),
                            BarData("비", statisticResponse.rainy.toInt()),
                            BarData("번개", statisticResponse.lightning.toInt()),
                        )

                        // 데이터 표시 함수 호출
                        bindWeatherData(dataList, binding.llAnalysisBarThisMonth, ::showBallViewThisMonth)
                    } else {
                        Log.e("이번 달 bar API Error", "Response body 비었음")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("이번 달 bar API Error", "Response Code: ${response.code()}, Error Body: $errorBody")
                }
            }

            override fun onFailure(call: Call<BaseResponse<StatisticResponse>>, t: Throwable) {
                Log.e("이번 달 bar API Failure", "Error: ${t.message}", t)
            }
        })
    }

    // 지난 달 통계
    private fun barStatisticsLastMonthApi() {
        val service = RetrofitImpl.authenticatedRetrofit.create(ReportService::class.java)
        val call = service.monthlyStatistic(ago = 1) // 지난 달

        call.enqueue(object : Callback<BaseResponse<StatisticResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<StatisticResponse>>,
                response: Response<BaseResponse<StatisticResponse>>,
            ) {
                if (response.isSuccessful) {
                    val statisticResponse = response.body()?.result // 'data'가 실제 응답 데이터를 담고 있는 필드일 경우
                    if (statisticResponse != null) {
                        Log.d("지난 달 bar API Success", "지난 달 Sunny: ${statisticResponse.sunny}, Cloudy: ${statisticResponse.cloudy}, Rainy: ${statisticResponse.rainy}, Lightning: ${statisticResponse.lightning}")

                        // 데이터 리스트 생성
                        val dataList = listOf(
                            BarData("맑음", statisticResponse.sunny.toInt()),
                            BarData("구름 약간", statisticResponse.cloudy.toInt()),
                            BarData("비", statisticResponse.rainy.toInt()),
                            BarData("번개", statisticResponse.lightning.toInt()),
                        )

                        // 데이터 표시 함수 호출
                        bindWeatherData(dataList, binding.llAnalysisBarLastMonth, ::showBallViewLastMonth)
                    } else {
                        Log.e("지난 달 bar API Error", "Response body 비었음")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("지난 달 bar API Error", "Response Code: ${response.code()}, Error Body: $errorBody")
                }
            }

            override fun onFailure(call: Call<BaseResponse<StatisticResponse>>, t: Throwable) {
                Log.e("지난 달 bar API Failure", "Error: ${t.message}", t)
            }
        })
    }

    // 월간 데이터 비교
    private fun weeklyComparisonApi() {
        val service = RetrofitImpl.authenticatedRetrofit.create(ReportService::class.java)
        val call = service.monthlyComparison(ago = 1)

        call.enqueue(object : Callback<BaseResponse<StatisticResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<StatisticResponse>>,
                response: Response<BaseResponse<StatisticResponse>>,
            ) {
                if (response.isSuccessful) {
                    val statisticResponse = response.body()?.result // 'data'가 실제 응답 데이터를 담고 있는 필드일 경우
                    if (statisticResponse != null) {
                        Log.d("월간 데이터 비교 API Success", "Sunny: ${statisticResponse.sunny}, Cloudy: ${statisticResponse.cloudy}, Rainy: ${statisticResponse.rainy}, Lightning: ${statisticResponse.lightning}")

                        val increases = mutableMapOf<String, Int>()
                        val decreases = mutableMapOf<String, Int>()

                        if (statisticResponse.sunny >= 0) increases["맑음"] = statisticResponse.sunny.toInt() else decreases["맑음"] = 0
                        if (statisticResponse.cloudy >= 0) increases["구름 약간"] = statisticResponse.cloudy.toInt() else decreases["구름 약간"] = 0
                        if (statisticResponse.rainy >= 0) increases["비"] = statisticResponse.rainy.toInt() else decreases["비"] = 15
                        if (statisticResponse.lightning >= 0) increases["번개"] = statisticResponse.lightning.toInt() else decreases["번개"] = 20

                        applyWeatherTextFormatting(increases, decreases)
                    } else {
                        Log.e("월간 데이터 비교 bar API Error", "Response body 비었음")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("월간 데이터 비교 bar API Error", "Response Code: ${response.code()}, Error Body: $errorBody")
                }
            }

            override fun onFailure(call: Call<BaseResponse<StatisticResponse>>, t: Throwable) {
                Log.e("월간 데이터 비교 bar API Failure", "Error: ${t.message}", t)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
