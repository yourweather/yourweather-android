package com.umc.yourweather.presentation.analysis

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.umc.yourweather.R
import com.umc.yourweather.data.entity.BarData
import com.umc.yourweather.databinding.FragmentBarStaticsWeeklyBinding

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

        // 텍스트 색 변환 코드
        val ssb1 = SpannableStringBuilder("맑음 비율이 20% 증가했습니다.")
        ssb1.apply {
            setSpan(ForegroundColorSpan(Color.parseColor("#70AD47")), 7, 10, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        }
        val ssb2 = SpannableStringBuilder("다소 흐림 비율이 10% 증가했습니다.")
        ssb2.apply {
            setSpan(ForegroundColorSpan(Color.parseColor("#70AD47")), 10, 13, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        }
        val ssb3 = SpannableStringBuilder("비 비율이 5% 감소했습니다.")
        ssb3.apply {
            setSpan(ForegroundColorSpan(Color.parseColor("#F7931E")), 6, 9, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        }
        val ssb4 = SpannableStringBuilder("번개 비율이 10% 감소했습니다.")
        ssb4.apply {
            setSpan(ForegroundColorSpan(Color.parseColor("#F7931E")), 7, 10, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        }
        binding.tvAnalysisDetailStaticsSunny.text = ssb1
        binding.tvAnalysisDetailStaticsCloudy.text = ssb2
        binding.tvAnalysisDetailStaticsRainy.text = ssb3
        binding.tvAnalysisDetailStaticsThunder.text = ssb4

        // 데이터 리스트 생성
        val dataList = listOf(
            BarData("맑음", 44),
            BarData("흐림", 60),
            BarData("비", 40),
            BarData("번개", 100),
            )

        val dataList2 = listOf(
            BarData("맑음", 52),
            BarData("흐림", 66),
            BarData("비", 38),
            BarData("번개", 90),
        )

        // 각 데이터 값에 해당하는 너비 계산
        val sum = dataList.sumOf { it.value }

        for (data in dataList) {
            val ratio = data.value.toFloat() / sum
            val width = (ratio * 100).toFloat()

            // 해당 데이터에 해당하는 View 생성 및 추가
            val view = View(requireContext())
            view.layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.MATCH_PARENT,
                width,
            )

            val drawableRes = when (data.label) {
                "맑음" -> R.drawable.bg_yellow_rec_round_sun
                "흐림" -> R.drawable.bg_gray_rec_cloud
                "비" -> R.drawable.bg_blue_rec_rain
                "번개" -> R.drawable.bg_darkblue_rec_round_thunder
                else -> R.drawable.bg_gray_rec_cloud
            }

            view.background = ContextCompat.getDrawable(requireContext(), drawableRes)
            binding.llAnalysisBarLastWeek.addView(view)

            // 클릭 시 말풍선 보이게
            view.setOnClickListener {
                showBallViewLastWeek(data.label)
            }
        }

        for (data in dataList2) {
            val ratio = data.value.toFloat() / sum
            val width = (ratio * 100).toFloat()

            // 해당 데이터에 해당하는 View 생성 및 추가
            val view = View(requireContext())
            view.layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.MATCH_PARENT,
                width,
            )

            val drawableRes = when (data.label) {
                "맑음" -> R.drawable.bg_yellow_rec_round_sun
                "흐림" -> R.drawable.bg_gray_rec_cloud
                "비" -> R.drawable.bg_blue_rec_rain
                "번개" -> R.drawable.bg_darkblue_rec_round_thunder
                else -> R.drawable.bg_gray_rec_cloud
            }

            view.background = ContextCompat.getDrawable(requireContext(), drawableRes)
            binding.llAnalysisBarThisWeek.addView(view)

            // 클릭 시 말풍선 보이게
            view.setOnClickListener {
                showBallViewThisWeek(data.label)
            }
        }
    }
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
    private fun hideBallViewAfterDelay(view: View) {
        val handler = Handler()
        handler.postDelayed({
            view.visibility = View.GONE
        }, 1500) // 1.5초 후에 말풍선을 숨김
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
