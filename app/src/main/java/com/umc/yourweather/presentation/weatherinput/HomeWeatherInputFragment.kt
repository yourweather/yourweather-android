package com.umc.yourweather.presentation.weatherinput

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.umc.yourweather.R
import com.umc.yourweather.databinding.FragmentHomeWeatherInputBinding

class HomeWeatherInputFragment : Fragment() {

    private lateinit var binding: FragmentHomeWeatherInputBinding
    private var listener: HomeFragmentInteractionListener? = null
    private var isButtonClicked = false
    private var isSeekBarAdjusted = false

    fun setListener(listener: HomeFragmentInteractionListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeWeatherInputBinding.inflate(inflater, container, false)
        setupUI()
        return binding.root
    }

    private fun setupUI() {
        val buttonAnimation: Animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.btn_weather_scale)

        binding.btnHomeSun.setOnClickListener {
            clearAnimations()
            it.startAnimation(buttonAnimation)
            isButtonClicked = true
            updateSaveButtonState()
            // 향후 클릭 시 추가할 동작 설정
        }

        binding.btnHomeCloud.setOnClickListener {
            clearAnimations()
            it.startAnimation(buttonAnimation)
            isButtonClicked = true
            updateSaveButtonState()
        }

        binding.btnHomeThunder.setOnClickListener {
            clearAnimations()
            it.startAnimation(buttonAnimation)
            isButtonClicked = true
            updateSaveButtonState()
        }

        binding.btnHomeRain.setOnClickListener {
            clearAnimations()
            it.startAnimation(buttonAnimation)
            isButtonClicked = true
            updateSaveButtonState()
        }

        //exit버튼 직접 클릭한 경우
        binding.btnHomeWeatherExit.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        //exit버튼이 담긴 영역 클릭한 경우
        binding.llWeatherInputExitL2.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnHomeWeatherSave.setOnClickListener {
            // 버튼이 활성화된 경우에만 클릭 리스너 동작
            if (isButtonClicked && isSeekBarAdjusted) {
                val homeFragment = HomeFragment()
                val transaction = parentFragmentManager.beginTransaction()
                transaction.replace(R.id.fl_initial_l1, homeFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }

        binding.seekbarHomeTemp.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                isSeekBarAdjusted = fromUser
                updateSaveButtonState()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // 필요한 경우 구현
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // 필요한 경우 구현
            }
        })
    }

    private fun clearAnimations() {
        binding.btnHomeSun.clearAnimation()
        binding.btnHomeCloud.clearAnimation()
        binding.btnHomeThunder.clearAnimation()
        binding.btnHomeRain.clearAnimation()
    }

    private fun updateSaveButtonState() {
        val context = requireContext()
        val isActive = isButtonClicked && isSeekBarAdjusted

        binding.btnHomeWeatherSave.isEnabled = isActive
        if (isActive) {
            binding.btnHomeWeatherSave.setImageResource(R.drawable.ic_home_save)
        } else {
            binding.btnHomeWeatherSave.setImageResource(R.drawable.ic_home_unsave)
        }
    }
}