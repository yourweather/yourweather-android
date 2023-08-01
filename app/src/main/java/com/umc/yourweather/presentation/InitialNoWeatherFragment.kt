
package com.umc.yourweather.presentation

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.umc.yourweather.R
import com.umc.yourweather.databinding.FragmentInitialNoWeatherBinding

class InitialNoWeatherFragment : Fragment() {
    lateinit var binding: FragmentInitialNoWeatherBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentInitialNoWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val customToastView = LayoutInflater.from(requireContext()).inflate(R.layout.toast_initial, null)

        val initialToast = Toast(requireContext())
        initialToast.view = customToastView

        initialToast.duration = Toast.LENGTH_LONG
        initialToast.setGravity(Gravity.BOTTOM or Gravity.CENTER, 0, resources.getDimensionPixelSize(R.dimen.initial_toast_margin_bottom))

        initialToast.show()

        binding.btnInitialWeather.setOnClickListener {
            val newFragment = HomeFragment() // 전환할 프래그먼트 생성
            Log.d("왜안되느", "$newFragment")
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fl_content, newFragment) // 프래그먼트 교체
            transaction.addToBackStack(null) // 백 스택에 추가하여 뒤로가기 버튼 동작을 지원
            transaction.commit()
        }
    }
}
