package com.umc.yourweather.presentation.analysis

import android.graphics.Color
import android.os.Bundle
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
import com.umc.yourweather.data.BarColor
import com.umc.yourweather.data.BarData
import com.umc.yourweather.databinding.FragmentBarStaticsMonthlyBinding

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

        // 텍스트 색 변환 코드
        val ssb1 = SpannableStringBuilder("맑음 비율이 20% 증가했습니다.")
        ssb1.apply{
            setSpan(ForegroundColorSpan(Color.parseColor("#70AD47")), 7,10, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        }
        val ssb2 = SpannableStringBuilder("다소 흐림 비율이 10% 증가했습니다.")
        ssb2.apply{
            setSpan(ForegroundColorSpan(Color.parseColor("#70AD47")), 10,13, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        }
        val ssb3 = SpannableStringBuilder("비 비율이 5% 감소했습니다.")
        ssb3.apply{
            setSpan(ForegroundColorSpan(Color.parseColor("#F7931E")), 6,9, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        }
        val ssb4 = SpannableStringBuilder("번개 비율이 10% 감소했습니다.")
        ssb4.apply{
            setSpan(ForegroundColorSpan(Color.parseColor("#F7931E")), 7,10, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        }
        binding.tvAnalysisDetailStaticsSunny.text = ssb1
        binding.tvAnalysisDetailStaticsCloudy.text = ssb2
        binding.tvAnalysisDetailStaticsRainy.text = ssb3
        binding.tvAnalysisDetailStaticsThunder.text = ssb4

        // 데이터 리스트 생성
        val dataList = listOf(
            BarData("맑음", 44),
            BarData("흐림", 29),
            BarData("번개", 59),
            BarData("비", 24),
        )

        val dataList2 = listOf(
            BarData("맑음", 14),
            BarData("흐림", 43),
            BarData("번개", 20),
            BarData("비", 66),
        )

        // 색상 리스트 생성
        val colorList = listOf(
            BarColor("맑음", "#FCC112"),
            BarColor("흐림", "#C7C7C7"),
            BarColor("번개", "#8299BB"),
            BarColor("비", "#1A1D34"),
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
            val colorResId = colorList.first { it.label == data.label }.colorResId
            view.setBackgroundColor(Color.parseColor(colorResId))
            binding.llAnalysisBarLastMonth.addView(view)
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
            val colorResId = colorList.first { it.label == data.label }.colorResId
            view.setBackgroundColor(Color.parseColor(colorResId))
            binding.llAnalysisBarThisMonth.addView(view)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
