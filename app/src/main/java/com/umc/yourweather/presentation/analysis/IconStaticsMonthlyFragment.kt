package com.umc.yourweather.presentation.analysis

import android.content.Intent
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
            val intent = Intent(activity, WrittenDetailListActivitySun::class.java)
            startActivity(intent)
        }
        binding.btnStaticsRightDetail2Monthly.setOnClickListener {
            val intent = Intent(activity, WrittenDetailListActivityCloud::class.java)
            startActivity(intent)
        }
        binding.btnStaticsRightDetail3Monthly.setOnClickListener {
            val intent = Intent(activity, WrittenDetailListActivityRain::class.java)
            startActivity(intent)
        }
        binding.btnStaticsRightDetail4Monthly.setOnClickListener {
            val intent = Intent(activity, WrittenDetailListActivityThunder::class.java)
            startActivity(intent)
        }
    }
}
