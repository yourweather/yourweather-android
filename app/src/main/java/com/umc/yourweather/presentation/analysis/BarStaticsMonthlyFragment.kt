package com.umc.yourweather.presentation.analysis

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

        // 데이터 리스트 생성
        val dataList = listOf(
            BarData("맑음", 44),
            BarData("흐림", 29),
            BarData("번개", 59),
            BarData("비", 24),
        )

        // 색상 리스트 생성
        val colorList = listOf(
            BarColor("맑음", R.color.black),
            BarColor("흐림", R.color.brown),
            BarColor("번개", R.color.orange),
            BarColor("비", R.color.green),
        )

        // 각 데이터 값에 해당하는 너비 계산
        val sum = dataList.sumBy { it.value }

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
            view.setBackgroundColor(ContextCompat.getColor(requireContext(), colorResId))
            binding.llAnalysisBarLastMonth.addView(view)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
