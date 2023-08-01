package com.umc.yourweather.presentation.analysis

import android.content.Intent
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_icon_statics_weekly, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentIconStaticsWeeklyBinding.bind(view)

        binding.btnStaticsRightDetail1Weekly.setOnClickListener {
            val intent = Intent(activity, WrittenDetailListActivitySunWeekly::class.java)
            startActivity(intent)
        }
        binding.btnStaticsRightDetail2Weekly.setOnClickListener {
            val intent = Intent(activity, WrittenDetailListActivityCloudWeekly::class.java)
            startActivity(intent)
        }
        binding.btnStaticsRightDetail3Weekly.setOnClickListener {
            val intent = Intent(activity, WrittenDetailListActivityRainWeekly::class.java)
            startActivity(intent)
        }
        binding.btnStaticsRightDetail4Weekly.setOnClickListener {
            val intent = Intent(activity, WrittenDetailListActivityThunderWeekly::class.java)
            startActivity(intent)
        }
    }
}
