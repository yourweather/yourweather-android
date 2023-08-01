package com.umc.yourweather.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.yourweather.databinding.FragmentHomeWeatherInputBinding

class HomeWeatherInputFragment : Fragment() {
    private lateinit var binding: FragmentHomeWeatherInputBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeWeatherInputBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // btn_home_weather_exit 버튼 클릭 이벤트 처리
        binding.btnHomeWeatherExit.setOnClickListener {
            // 이전 프래그먼트로 돌아가기
            parentFragmentManager.popBackStack()
        }
        // btn_home_weather_save 버튼 클릭 이벤트 처리
        binding.btnHomeWeatherSave.setOnClickListener {
            (requireActivity() as? HomeFragment.HomeFragmentInteractionListener)?.goToNewHome()
        }
    }
}
