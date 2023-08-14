package com.umc.yourweather.presentation.analysis

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
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
import androidx.fragment.app.Fragment
import com.umc.yourweather.R
import com.umc.yourweather.data.entity.BarData
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.StatisticResponse
import com.umc.yourweather.data.service.ReportService
import com.umc.yourweather.databinding.FragmentBarStaticsWeeklyBinding
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

        CoroutineScope(Dispatchers.Main).launch {
            // 지난 주, 이번 주 정확한 달 숫자
            val previousWeekText = getPreviousWeekText()
            val currentWeekText = getCurrentWeekText()
            binding.tvAnalysisMonthNum.text = previousWeekText
            binding.tvAnalysisMonthThisNum.text = currentWeekText

            barStatisticsThisWeekApi()
            barStatisticsLastWeekApi()
            weeklyComparisonApi()
        }
    }

    private fun getPreviousWeekText(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.WEEK_OF_YEAR, -1)

        val dateFormat = SimpleDateFormat("M.d", Locale.getDefault())

        // Find the first day of the previous week (Monday)
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DAY_OF_WEEK, -1)
        }
        val previousWeekStart = dateFormat.format(calendar.time)

        // Find the last day of the previous week (Sunday)
        calendar.add(Calendar.DAY_OF_WEEK, 6)
        val previousWeekEnd = dateFormat.format(calendar.time)

        return "$previousWeekStart ~ $previousWeekEnd"
    }

    private fun getCurrentWeekText(): String {
        val calendar = Calendar.getInstance()

        val dateFormat = SimpleDateFormat("M.d", Locale.getDefault())

        // Find the first day of the current week (Monday)
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DAY_OF_WEEK, -1)
        }
        val currentWeekStart = dateFormat.format(calendar.time)

        // Find the last day of the current week (Sunday)
        calendar.add(Calendar.DAY_OF_WEEK, 6)
        val currentWeekEnd = dateFormat.format(calendar.time)

        return "$currentWeekStart ~ $currentWeekEnd"
    }

    // 날씨 통계 색상 변환 함수
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
            val color = if (increaseValue > 0) increaseColor else decreaseColor

            if (increaseValue == 0 && decreaseValue == 0) {
                textView.text = "$weather 비율이 동일합니다."
            } else {
                val actionText =
                    if (increaseValue > 0) "증가" else if (decreaseValue > 0) "감소" else "변화가 없습니다"
                val changeValue = if (increaseValue > 0) increaseValue else decreaseValue

                val arrowText =
                    SpannableStringBuilder("$weather 비율이 $changeValue% ${actionText}했습니다.")

                val changeValueStart = arrowText.indexOf("$changeValue%")
                val changeValueEnd = changeValueStart + "$changeValue%".length
                arrowText.apply {
                    setSpan(
                        ForegroundColorSpan(Color.parseColor(color)),
                        changeValueStart,
                        changeValueEnd,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
                    )
                }
                textView.text = arrowText
            }

//                val spannableText = SpannableStringBuilder()
//                    .append("$weather 비율이 ")
//                    .apply {
//                        append("$changeValue%")
//                        setSpan(
//                            ForegroundColorSpan(Color.parseColor(color)),
//                            length - changeValue.toString().length - 1,
//                            length - 1,
//                            Spannable.SPAN_EXCLUSIVE_INCLUSIVE,
//                        )
//                    }
//                    .append(" $actionText")
//                    .apply {
//                        if (actionText != "변화가 없습니다.") {
//                            append("했습니다.")
//                        }
//                    }
//                spannableText

            // textView.text = formattedText

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
        val sum = dataList.sumOf { it.value }

        // Check if only one non-zero weather exists
        val isOnlyOneNonZeroWeather = dataList.count { it.value != 0 } == 1

        for ((index, data) in dataList.withIndex()) {
            val value = data.value
            val ratio = if (sum != 0) value.toDouble() / sum else 0.0
            val width: Float = (ratio.toFloat() * 100)

            val view = View(requireContext())

            val roundedCornerDrawable = if (isOnlyOneNonZeroWeather) {
                getRoundedCornerDrawableForAllCorners(data.label, dataList.size)
            } else {
                getRoundedCornerDrawableForWeather(data.label, index, dataList.size)
            }
            view.background = roundedCornerDrawable

            val layoutParams = LinearLayout.LayoutParams(
                width.toInt(),
                ViewGroup.LayoutParams.MATCH_PARENT,
                width,
            )

            view.layoutParams = layoutParams

            layout.addView(view)

            view.setOnClickListener {
                clickListener(data.label)
            }

            // Add a gray divider if the value is not 0 and there's a next item with non-zero value
            if (value != 0 && (index < dataList.size - 1 && dataList[index + 1].value != 0)) {
                val dividerView = View(requireContext())
                val dividerLayoutParams = LinearLayout.LayoutParams(
                    resources.getDimensionPixelSize(R.dimen.divider_width),
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                dividerView.layoutParams = dividerLayoutParams
                dividerView.setBackgroundColor(Color.parseColor("#F1F1F1")) // Gray color
                layout.addView(dividerView)
            }
        }
    }

    private fun getRoundedCornerDrawableForAllCorners(weatherLabel: String, dataSize: Int): Drawable {
        val color = getColorForWeather(weatherLabel)
        val cornerRadius = floatArrayOf(
            resources.getDimension(R.dimen.rounded_corner_radius),
            resources.getDimension(R.dimen.rounded_corner_radius),
            resources.getDimension(R.dimen.rounded_corner_radius),
            resources.getDimension(R.dimen.rounded_corner_radius),
            resources.getDimension(R.dimen.rounded_corner_radius),
            resources.getDimension(R.dimen.rounded_corner_radius),
            resources.getDimension(R.dimen.rounded_corner_radius),
            resources.getDimension(R.dimen.rounded_corner_radius),
        )

        val shapeDrawable = GradientDrawable()
        shapeDrawable.setColor(color)
        shapeDrawable.cornerRadii = cornerRadius
        shapeDrawable.shape = GradientDrawable.RECTANGLE

        return shapeDrawable
    }


    private fun getRoundedCornerDrawableForWeather(weatherLabel: String, index: Int, dataSize: Int): Drawable {
        val color = getColorForWeather(weatherLabel)
        val cornerRadius = when (index) {
            0 -> {
                // First item, round left corner
                floatArrayOf(
                    resources.getDimension(R.dimen.rounded_corner_radius),
                    resources.getDimension(R.dimen.rounded_corner_radius),
                    0f,
                    0f,
                    0f,
                    0f,
                    resources.getDimension(R.dimen.rounded_corner_radius),
                    resources.getDimension(R.dimen.rounded_corner_radius),
                )
            }
            dataSize - 1 -> {
                // Last item, round right corner
                floatArrayOf(
                    0f,
                    0f,
                    resources.getDimension(R.dimen.rounded_corner_radius),
                    resources.getDimension(R.dimen.rounded_corner_radius),
                    resources.getDimension(R.dimen.rounded_corner_radius),
                    resources.getDimension(R.dimen.rounded_corner_radius),
                    0f,
                    0f,
                )
            }
            else -> {
                // Middle items, no corners
                null
            }
        }

        val shapeDrawable = GradientDrawable()
        shapeDrawable.setColor(color)
        shapeDrawable.cornerRadii = cornerRadius
        shapeDrawable.shape = GradientDrawable.RECTANGLE

        return shapeDrawable
    }

    private fun getColorForWeather(weatherLabel: String): Int {
        return when (weatherLabel) {
            "맑음" -> Color.parseColor("#FCC112")
            "구름 약간" -> Color.parseColor("#C7C7C7")
            "비" -> Color.parseColor("#8299BB")
            "번개" -> Color.parseColor("#1A1D34")
            else -> Color.parseColor("#A0A0A0")
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
            "구름 약간" -> {
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
            "구름 약간" -> {
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
        val call = service.weeklyStatistic(ago = 0) // 이번 주

        call.enqueue(object : Callback<BaseResponse<StatisticResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<StatisticResponse>>,
                response: Response<BaseResponse<StatisticResponse>>,
            ) {
                if (response.isSuccessful) {
                    val statisticResponse = response.body()?.result
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
