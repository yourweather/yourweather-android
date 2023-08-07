package com.umc.yourweather.presentation.analysis

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.yourweather.R
import com.umc.yourweather.databinding.FragmentIconStaticsMonthlyBinding
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
        setupOnClickListeners()
    }

    private fun setupOnClickListeners() {
        binding.btnStaticsRightDetail1Monthly.setOnClickListener {
            replaceFragment(WrittenDetailListFragmentSun())
        }
        binding.btnStaticsRightDetail2Monthly.setOnClickListener {
            replaceFragment(WrittenDetailListFragmentCloud())
        }
        binding.btnStaticsRightDetail3Monthly.setOnClickListener {
            replaceFragment(WrittenDetailListFragmentRain())
        }
        binding.btnStaticsRightDetail4Monthly.setOnClickListener {
            replaceFragment(WrittenDetailListFragmentThunder())
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
