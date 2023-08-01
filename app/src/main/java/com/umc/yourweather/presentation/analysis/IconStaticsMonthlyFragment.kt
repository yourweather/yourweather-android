package com.umc.yourweather.presentation.analysis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.yourweather.R
import com.umc.yourweather.databinding.FragmentIconStaticsMonthlyBinding

class IconStaticsMonthlyFragment : Fragment() {
    private var _binding: FragmentIconStaticsMonthlyBinding? = null
    private val binding: FragmentIconStaticsMonthlyBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_icon_statics_monthly, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentIconStaticsMonthlyBinding.bind(view)

        binding.btnStaticsRightDetail1Monthly.setOnClickListener {
            val mFragment = WrittenDetailListFragmentSun()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_content, mFragment)
                .addToBackStack(null)
                .commit()
        }
        binding.btnStaticsRightDetail2Monthly.setOnClickListener {
            val mFragment = WrittenDetailListFragmentCloud()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_content, mFragment)
                .addToBackStack(null)
                .commit()
        }
        binding.btnStaticsRightDetail3Monthly.setOnClickListener {
            val mFragment = WrittenDetailListFragmentRain()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_content, mFragment)
                .addToBackStack(null)
                .commit()
        }
        binding.btnStaticsRightDetail4Monthly.setOnClickListener {
            val mFragment = WrittenDetailListFragmentThunder()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_content, mFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}
