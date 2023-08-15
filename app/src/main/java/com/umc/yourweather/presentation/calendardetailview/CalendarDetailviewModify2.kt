package com.umc.yourweather.presentation.calendardetailview

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivityCalendarDetailviewModify2Binding
class CalendarDetailviewModify2 : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarDetailviewModify2Binding
    private lateinit var cardView: CardView
    private lateinit var editText: AppCompatEditText
    private var isSeekBarAdjusted = false // 변수 선언

    interface CalendarDetailviewModify2Listener {
        fun onWeatherButtonClicked(weatherType: String)
    }

    private var listener: CalendarDetailviewModify2Listener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarDetailviewModify2Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 초기에 보여지는 화면
        cardView = binding.cvCalendardetailviewModify2
        editText = binding.editText as AppCompatEditText
        // seekBar 보여죽기
        setupSeekBarListener()
        // 카드뷰 보여주기
        setupCardViewClickListener()
        // 날씨 아이콘 보여주기
        setupWeatherButtons()
        // 시간 선택 창
        setupTimePicker()
    }

    private fun setupSeekBarListener() {
        val seekBar = binding.seekbarCalendarDetailviewTemp2

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
    }

    private fun setupCardViewClickListener() {
        cardView.setOnClickListener {
            showKeyboardAndFocusEditText()
        }
    }

    private fun setupWeatherButtons() {
        val buttonAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.btn_weather_scale)

        binding.btnHomeSun.setOnClickListener {
            animateAndHandleButtonClick(binding.btnHomeCloud)
        }

        binding.btnHomeCloud.setOnClickListener {
            animateAndHandleButtonClick(binding.btnHomeCloud)
            updateButtonInFragmentColor(R.color.sorange)
        }

        binding.btnHomeThunder.setOnClickListener {
            animateAndHandleButtonClick(binding.btnHomeCloud)
            updateButtonInFragmentColor(R.color.sorange)
        }

        binding.btnHomeRain.setOnClickListener {
            animateAndHandleButtonClick(binding.btnHomeCloud)
            updateButtonInFragmentColor(R.color.sorange)
        }
    }

    private fun animateAndHandleButtonClick(button: Button) {
        val buttonAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.btn_weather_scale)

        binding.btnHomeSun.clearAnimation()
        binding.btnHomeCloud.clearAnimation()
        binding.btnHomeThunder.clearAnimation()
        binding.btnHomeRain.clearAnimation()

        button.startAnimation(buttonAnimation)
    }

    private fun updateButtonInFragmentColor(colorId: Int) {
        val fragment1: Fragment = ModifyFragment2()
        val buttonInFragment = fragment1.view?.findViewById<Button>(R.id.btn_calendardetailview_save)
        buttonInFragment?.setTextColor(ContextCompat.getColor(this, colorId))
    }

    private fun setupTimePicker() {
        val timePicker: TimePicker = findViewById(R.id.tp_calendardetailview)

        val selectedHour: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.hour
        } else {
            timePicker.currentHour
        }

        val selectedMinute: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.minute
        } else {
            timePicker.currentMinute
        }

        val selectedAmPm: String = if (selectedHour >= 12) {
            "오후"
        } else {
            "오전"
        }

        val tvTime: TextView = findViewById(R.id.tv_calendar_detailview_modify2_time)
        tvTime.text = ("$selectedAmPm $selectedHour:${selectedMinute}시")
    }

    // 미입력 내역에서 넘어오는 date 값 처리하는 로직 추가
    private fun handleUnwrittenDate() {
        val unWrittenDate = intent.getStringExtra("unWrittenDate")
        if (unWrittenDate != null) {
            // receivedDate를 활용하여 처리하는 로직을 작성하세요
        }
    }

    private fun updateSaveButtonState() {
        val fragment1: Fragment? = supportFragmentManager.findFragmentById(R.id.fragment_container)
        val buttonInFragment = fragment1?.view?.findViewById<Button>(R.id.btn_calendardetailview_save)

        val isActive = isSeekBarAdjusted

        buttonInFragment?.isEnabled = isActive
        if (isActive) {
            buttonInFragment?.setTextColor(ContextCompat.getColor(this, R.color.sorange))
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
