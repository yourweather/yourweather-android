package com.umc.yourweather.presentation.weatherinput

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import com.umc.yourweather.R
import com.umc.yourweather.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), HomeFragmentInteractionListener {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this)
            .load(R.raw.motion_home_sun).into(binding.motionHomeWeather)

        binding.btnHomeWeatherinput.setOnClickListener {
            openHomeWeatherInputFragment()
        }
        showHomeToast()
    }

    private fun openHomeWeatherInputFragment() {
        val fragment = HomeWeatherInputFragment()
        fragment.setListener(this)
        parentFragmentManager.commit {
            replace(R.id.fl_home_l1, fragment)
            addToBackStack(null)
        }
    }

    override fun goToNewHome() {
        showHomeToast()
        parentFragmentManager.popBackStackImmediate()
    }

    private fun showHomeToast() {
        Log.d("HomeFragment", "홈토스트보여줘")
        val customToastView = LayoutInflater.from(requireContext()).inflate(R.layout.toast_home, null)

        val homeToast = Toast(requireContext())
        homeToast.view = customToastView

        homeToast.duration = Toast.LENGTH_SHORT

        homeToast.setGravity(android.view.Gravity.BOTTOM or android.view.Gravity.CENTER, 0, resources.getDimensionPixelSize(R.dimen.home_toast_margin_bottom))

        homeToast.show()
    }
}
