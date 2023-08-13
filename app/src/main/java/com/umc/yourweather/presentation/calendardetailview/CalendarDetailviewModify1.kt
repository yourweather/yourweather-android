package com.umc.yourweather.presentation.calendardetailview

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatEditText
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivityCalendarDetailviewModify1Binding
import com.umc.yourweather.databinding.ActivityCalendarDetailviewModify2Binding
import com.umc.yourweather.presentation.weatherinput.HomeFragment


class CalendarDetailviewModify1 : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarDetailviewModify1Binding
    private lateinit var cardView: CardView
    private lateinit var editText: AppCompatEditText
    private var isSeekBarAdjusted = false // 변수 선언

    interface CalendarDetailviewModify1Listener {
        fun onWeatherButtonClicked(weatherType: String)
    }

    private var listener: CalendarDetailviewModify1Listener?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarDetailviewModify1Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val seekBar = binding.seekbarCalendarDetailviewTemp

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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

        cardView = binding.cvCalendardetailviewModify1
        editText = binding.editTextModify1 as AppCompatEditText

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
        val buttonInFragment = fragment1.view?.findViewById<Button>(R.id.btn_calendardetailview_save)

        // 각 버튼과 애니메이션 연결
        binding.btnHomeSun.setOnClickListener {
            binding.btnHomeCloud.clearAnimation()
            binding.btnHomeThunder.clearAnimation()
            binding.btnHomeRain.clearAnimation()

            it.startAnimation(buttonAnimation)
            // 향후 클릭 시 추가할 동작 설정
            // 프래그먼트의 버튼 참조
            val buttonInFragment = fragment1.view?.findViewById<Button>(R.id.btn_calendardetailview_save)
            // 버튼의 텍스트 색상 변경
            buttonInFragment?.setTextColor(ContextCompat.getColor(this, R.color.sorange))
        }

        binding.btnHomeCloud.setOnClickListener {
            binding.btnHomeSun.clearAnimation()
            binding.btnHomeThunder.clearAnimation()
            binding.btnHomeRain.clearAnimation()

            it.startAnimation(buttonAnimation)
            // 프래그먼트의 버튼 참조
            val buttonInFragment = fragment1.view?.findViewById<Button>(R.id.btn_calendardetailview_save)
            // 버튼의 텍스트 색상 변경
            buttonInFragment?.setTextColor(ContextCompat.getColor(this, R.color.sorange))

        }

        binding.btnHomeThunder.setOnClickListener {
            binding.btnHomeSun.clearAnimation()
            binding.btnHomeCloud.clearAnimation()
            binding.btnHomeRain.clearAnimation()

            it.startAnimation(buttonAnimation)

            // 프래그먼트의 버튼 참조
            val buttonInFragment = fragment1.view?.findViewById<Button>(R.id.btn_calendardetailview_save)
            // 버튼의 텍스트 색상 변경
            buttonInFragment?.setTextColor(ContextCompat.getColor(this, R.color.sorange))

        }

        binding.btnHomeRain.setOnClickListener {
            binding.btnHomeSun.clearAnimation()
            binding.btnHomeCloud.clearAnimation()
            binding.btnHomeThunder.clearAnimation()

            it.startAnimation(buttonAnimation)

            // 프래그먼트의 버튼 참조
            val buttonInFragment = fragment1.view?.findViewById<Button>(R.id.btn_calendardetailview_save)
            // 버튼의 텍스트 색상 변경
            buttonInFragment?.setTextColor(ContextCompat.getColor(this, R.color.sorange))

        }

// 오렌지로 텍스트 색상이 변경되었을 때만 다른 화면으로 전환
        if (buttonInFragment?.currentTextColor == ContextCompat.getColor(this, R.color.sorange)) {
            // 화면 전환 코드 작성
            // 예를 들어,
          //  val intent = Intent(this, AnotherActivity::class.java)
            startActivity(intent)
        }


    }

    private fun updateSaveButtonState() {
        val fragment1: Fragment? = supportFragmentManager.findFragmentById(R.id.fragment_container)
        val buttonInFragment = fragment1?.view?.findViewById<Button>(R.id.btn_calendardetailview_save)


        val isActive = isSeekBarAdjusted

        buttonInFragment?.isEnabled = isActive
        if (isActive) {
            buttonInFragment?.setTextColor(ContextCompat.getColor(this, R.color.sorange))
            //  val intent = Intent(this, AnotherActivity::class.java)
            startActivity(intent)
        } else {
            buttonInFragment?.setTextColor(ContextCompat.getColor(this, R.color.gray))
        }
    }
    private fun showKeyboardAndFocusEditText() {
        // 키보드 보여주기
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)

        // EditText에 포커스 주기
        editText.requestFocus()
    }

}