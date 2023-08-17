package com.umc.yourweather.presentation.share

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umc.yourweather.R
import com.umc.yourweather.presentation.weatherinput.HomeFragment

class SaveHomeImgFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_save_home_img, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ll_save_home_download 버튼 클릭
        view.findViewById<View>(R.id.ll_save_home_download)?.setOnClickListener {
            // 해당 프래그먼트를 종료하고 이전 화면으로 돌아가기
            parentFragmentManager.popBackStack()

            // 홈 프래그먼트로 이동
            val homeFragment = HomeFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fl_home_l1, homeFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}
