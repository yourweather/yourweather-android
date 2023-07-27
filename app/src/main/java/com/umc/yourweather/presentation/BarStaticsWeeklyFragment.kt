package com.umc.yourweather.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.yourweather.R
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
        return inflater.inflate(R.layout.fragment_bar_statics_weekly, container, false)
    }
}
