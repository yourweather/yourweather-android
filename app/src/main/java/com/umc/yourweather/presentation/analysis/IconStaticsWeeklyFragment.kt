package com.umc.yourweather.presentation.analysis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.yourweather.R
import com.umc.yourweather.databinding.FragmentIconStaticsWeeklyBinding

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
        setupOnClickListeners()
    }

    private fun setupOnClickListeners() {
        // sun 상세 리스트 넘어가기
        binding.llSunWeekly.setOnClickListener {
            replaceFragment(WrittenDetailListFragmentSunWeekly())
        }
        binding.btnStaticsRightDetail1Weekly.setOnClickListener {
            replaceFragment(WrittenDetailListFragmentSunWeekly())
        }

        // cloud 상세 리스트 넘어가기
        binding.llCloudWeekly.setOnClickListener {
            replaceFragment(WrittenDetailListFragmentCloudWeekly())
        }
        binding.btnStaticsRightDetail2Weekly.setOnClickListener {
            replaceFragment(WrittenDetailListFragmentSunWeekly())
        }

        // rain 상세 리스트 넘어가기
        binding.llRainWeekly.setOnClickListener {
            replaceFragment(WrittenDetailListFragmentRainWeekly())
        }
        binding.btnStaticsRightDetail3Weekly.setOnClickListener {
            replaceFragment(WrittenDetailListFragmentSunWeekly())
        }

        // thunder 상세 리스트 넘어가기
        binding.llThunderWeekly.setOnClickListener {
            replaceFragment(WrittenDetailListFragmentThunderWeekly())
        }
        binding.btnStaticsRightDetail4Weekly.setOnClickListener {
            replaceFragment(WrittenDetailListFragmentSunWeekly())
        }
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

    }
    private fun updateTitleAndFetchStatistics(weekAgo: Int) {
        val ago = weekAgo
        binding.tvUnwrittenTitleWeekly.text = "${ago}주 전"
        // barStatisticsThisWeekApi(ago)

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
