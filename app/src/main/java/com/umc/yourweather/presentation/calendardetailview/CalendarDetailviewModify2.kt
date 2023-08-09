package com.umc.yourweather.presentation.calendardetailview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivityCalendarDetailviewModify1Binding
import com.umc.yourweather.databinding.ActivityCalendarDetailviewModify2Binding

class CalendarDetailviewModify2 : AppCompatActivity() {
    private val TAG = "CalendarDetailviewModify2"
    private lateinit var binding: ActivityCalendarDetailviewModify2Binding
    private lateinit var cardView: CardView
    private lateinit var editText: AppCompatEditText

    interface CalendarDetailviewModify2Listener {
        fun onWeatherButtonClicked(weatherType: String)
    }

    private var listener: CalendarDetailviewModify2Listener?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarDetailviewModify2Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        cardView = binding.cvCalendardetailviewModify2
        editText = binding.editText as AppCompatEditText

        cardView.setOnClickListener {
            showKeyboardAndFocusEditText()
        }

        // 프래그먼트를 추가하고 초기화
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        // 프래그먼트 객체 생성
        val fragment1: Fragment = ModifyFragment2()

        // 프래그먼트를 레이아웃 컨테이너에 추가
        fragmentTransaction.add(R.id.fragment_container, fragment1)

        // 프래그먼트 트랜잭션 완료
        fragmentTransaction.commit()

        // 애니메이션 리소스 가져오기
        val buttonAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.btn_weather_scale)


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




    }

    private fun showKeyboardAndFocusEditText() {
        TODO("Not yet implemented")
    }


}