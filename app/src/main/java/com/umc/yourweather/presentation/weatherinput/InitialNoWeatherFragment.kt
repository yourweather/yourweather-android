
package com.umc.yourweather.presentation.weatherinput

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.yourweather.R
import com.umc.yourweather.databinding.FragmentInitialNoWeatherBinding
import com.umc.yourweather.di.UserSharedPreferences

class InitialNoWeatherFragment : Fragment() {
    private lateinit var binding: FragmentInitialNoWeatherBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentInitialNoWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnInitialWeather.setOnClickListener {
            val newFragment = HomeWeatherInputFragment()
            val transaction = parentFragmentManager.beginTransaction()

            // 기존 프래그먼트를 백스택에서 제거
            parentFragmentManager.popBackStack()

            // 새로운 프래그먼트를 컨테이너에 추가
            transaction.add(R.id.fl_initial_l1, newFragment)

            transaction.addToBackStack(null)
            transaction.commit()
        }


        val userNickname = UserSharedPreferences.getUserNickname(requireContext())
        binding.tvInitialUsername.text = userNickname
    }
}
