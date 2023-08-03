package com.umc.yourweather.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.umc.yourweather.R
import com.umc.yourweather.databinding.FragmentHomeWeatherInputBinding

class HomeWeatherInputFragment : Fragment() {
    private lateinit var binding: FragmentHomeWeatherInputBinding
    private var listener: HomeFragmentInteractionListener? = null

    fun setListener(listener: HomeFragmentInteractionListener) {
        this.listener = listener
    }
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

        // 애니메이션 리소스 가져오기
        val buttonAnimation: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.btn_weather_scale)

        // 각 버튼과 애니메이션 연결
        binding.btnHomeSun.setOnClickListener {
            binding.btnHomeCloud.clearAnimation()
            binding.btnHomeThunder.clearAnimation()
            binding.btnHomeRain.clearAnimation()

            it.startAnimation(buttonAnimation)
            // 향후 클릭 시 추가할 동작 설정
        }

        binding.btnHomeCloud.setOnClickListener {
            binding.btnHomeSun.clearAnimation()
            binding.btnHomeThunder.clearAnimation()
            binding.btnHomeRain.clearAnimation()

            it.startAnimation(buttonAnimation)

        }

        binding.btnHomeThunder.setOnClickListener {
            binding.btnHomeSun.clearAnimation()
            binding.btnHomeCloud.clearAnimation()
            binding.btnHomeRain.clearAnimation()

            it.startAnimation(buttonAnimation)

        }

        binding.btnHomeRain.setOnClickListener {
            binding.btnHomeSun.clearAnimation()
            binding.btnHomeCloud.clearAnimation()
            binding.btnHomeThunder.clearAnimation()

            it.startAnimation(buttonAnimation)

        }

        binding.btnHomeWeatherExit.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnHomeWeatherSave.setOnClickListener {
            val homeFragment = HomeFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fl_initial_l1, homeFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}
