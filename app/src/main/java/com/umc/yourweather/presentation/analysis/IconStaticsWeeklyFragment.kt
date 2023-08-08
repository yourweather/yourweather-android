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
        binding.btnStaticsRightDetail1Weekly.setOnClickListener {
            replaceFragment(WrittenDetailListFragmentSunWeekly())
        }
        binding.btnStaticsRightDetail2Weekly.setOnClickListener {
            replaceFragment(WrittenDetailListFragmentRainWeekly())
        }
        binding.btnStaticsRightDetail3Weekly.setOnClickListener {
            replaceFragment(WrittenDetailListFragmentCloudWeekly())
        }
        binding.btnStaticsRightDetail4Weekly.setOnClickListener {
            replaceFragment(WrittenDetailListFragmentThunderWeekly())
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
