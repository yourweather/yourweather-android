package com.umc.yourweather.presentation.analysis

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.umc.yourweather.R
import com.umc.yourweather.data.BarColor
import com.umc.yourweather.data.BarData
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
        return binding.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 데이터 리스트 생성
        val dataList = listOf(
            BarData("맑음", 44),
            BarData("흐림", 2),
            BarData("번개", 100),
            BarData("비", 24),
        )

        val dataList2 = listOf(
            BarData("맑음", 100),
            BarData("흐림", 34),
            BarData("번개", 20),
            BarData("비", 66),
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
                "번개" -> R.drawable.bg_blue_rec_rain
                "비" -> R.drawable.bg_darkblue_rec_round_thunder
                else -> R.drawable.bg_gray_rec_cloud
            }

            view.background = ContextCompat.getDrawable(requireContext(), drawableRes)
            binding.llAnalysisBarLastWeek.addView(view)
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
                "번개" -> R.drawable.bg_blue_rec_rain
                "비" -> R.drawable.bg_darkblue_rec_round_thunder
                else -> R.drawable.bg_gray_rec_cloud
            }

            view.background = ContextCompat.getDrawable(requireContext(), drawableRes)
            binding.llAnalysisBarThisWeek.addView(view)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
