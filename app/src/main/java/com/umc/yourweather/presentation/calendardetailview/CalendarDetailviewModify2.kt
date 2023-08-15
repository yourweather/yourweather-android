package com.umc.yourweather.presentation.calendardetailview

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.SeekBar
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.umc.yourweather.R
import com.umc.yourweather.data.enums.Status
import com.umc.yourweather.data.remote.request.MemoRequest
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.MemoResponse
import com.umc.yourweather.data.service.MemoService
import com.umc.yourweather.databinding.ActivityCalendarDetailviewModify2Binding
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.di.UserSharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
        cardView.setOnClickListener {
            showKeyboardAndFocusEditText()
        }
        // 날씨 아이콘 보여주기
        setupWeatherButtons()
        // 시간 선택 창
        // setupTimePicker()

        // 미입력에서 넘어오는 날짜
        val unWrittenDate = intent.getStringExtra("unWrittenDate")
        if (unWrittenDate != null) {
            val dateParts = unWrittenDate.split("-")
            if (dateParts.size == 3) {
                val year = dateParts[0].toInt()
                val month = dateParts[1].toInt()
                val day = dateParts[2].toInt()

                binding.tvDetailviewModify2Date.text = "${month}월 ${day}의 기록"
            }
        }

//
//        val status: Status = Status.YOUR_STATUS // Status 열거형에 맞게 설정
//        val content: String? = "메모 내용"
//        val localDateTime: String? = "2023-08-16T12:34:56" // 예시 날짜와 시간
//        val temperature: Int? = 25
//
//        // writeMemo 함수 호출
//        writeMemo(status, content, localDateTime, temperature)

        binding.btnCalendardetailviewSave.setOnClickListener {
            val status: Status = selectedStatus // Status 열거형에 맞게 설정
            val content: String? = editText.text?.toString() // editText에서 입력한 내용 가져오기
            val localDateTime: String? = getCurrentDateTime() // 저장 버튼 누른 시간
            val temperature: Int? = binding.seekbarCalendarDetailviewTemp2.progress // seekBar의 진행 상태 가져오기

            // writeMemo 함수 호출
            writeMemo(status, content, localDateTime, temperature)
        }
    }

    // 메모 요청 API
    private fun writeMemo(status: Status, content: String?, localDateTime: String?, temperature: Int?) {
        val MemoService = RetrofitImpl.authenticatedRetrofit.create(MemoService::class.java)
        MemoService.memoWrite(MemoRequest(status, content, localDateTime, temperature))
            .enqueue(object : Callback<BaseResponse<MemoResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<MemoResponse>>,
                    response: Response<BaseResponse<MemoResponse>>,
                ) {
                    if (response.isSuccessful) {
                        val MemoResponse = response.body()?.result
                        Log.d("메모 작성", "메모 작성, 전달 성공 ${response.body()?.result}")
                    } else {
                        Log.d("메모 작성 실패", "메모 작성, 전달 성공 실패: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<BaseResponse<MemoResponse>>, t: Throwable) {
                    Log.d("메모 작성 요청", "메모 작성 요청 실패: ${t.message}")
                }
            })
    }

    // 현재 시간을 가져오는 함수
    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val now = Calendar.getInstance().time
        return dateFormat.format(now)
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
                val isActive = isSeekBarAdjusted

                binding.btnCalendardetailviewSave.setTextColor(ContextCompat.getColor(this@CalendarDetailviewModify2, R.color.sorange))
                if (isActive) {
                    binding.btnCalendardetailviewSave.setTextColor(ContextCompat.getColor(this@CalendarDetailviewModify2, R.color.sorange))
                } else {
                    binding.btnCalendardetailviewSave.setTextColor(ContextCompat.getColor(this@CalendarDetailviewModify2, R.color.gray))
                }
            }
        })
    }

    private var selectedStatus: Status = Status.SUNNY // 기본값으로 SUNNY 설정

    private fun setupWeatherButtons() {

        binding.btnHomeSun.setOnClickListener {
            selectedStatus = Status.SUNNY
            animateAndHandleButtonClick(binding.btnHomeSun)
        }

        binding.btnHomeCloud.setOnClickListener {
            selectedStatus = Status.CLOUDY
            animateAndHandleButtonClick(binding.btnHomeCloud)
            updateButtonInFragmentColor(R.color.sorange)
        }

        binding.btnHomeThunder.setOnClickListener {
            selectedStatus = Status.LIGHTNING
            animateAndHandleButtonClick(binding.btnHomeThunder)
            updateButtonInFragmentColor(R.color.sorange)
        }

        binding.btnHomeRain.setOnClickListener {
            selectedStatus = Status.RAINY
            animateAndHandleButtonClick(binding.btnHomeRain)
            updateButtonInFragmentColor(R.color.sorange)
        }
    }

    // 날씨 아이콘 애니메이션
    private fun animateAndHandleButtonClick(button: Button) {
        val buttonAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.btn_weather_scale)

        binding.btnHomeSun.clearAnimation()
        binding.btnHomeCloud.clearAnimation()
        binding.btnHomeThunder.clearAnimation()
        binding.btnHomeRain.clearAnimation()

        button.startAnimation(buttonAnimation)
    }

    // 버튼 활성화
    private fun updateButtonInFragmentColor(colorId: Int) {
        val fragment1: Fragment = ModifyFragment2()
        val buttonInFragment = fragment1.view?.findViewById<Button>(R.id.btn_calendardetailview_save)
        buttonInFragment?.setTextColor(ContextCompat.getColor(this, colorId))
    }

//    // 시간 선택 창
//    private fun setupTimePicker() {
//        val timePicker: TimePicker = findViewById(R.id.tp_calendardetailview)
//
//        val selectedHour: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            timePicker.hour
//        } else {
//            timePicker.currentHour
//        }
//
//        val selectedMinute: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            timePicker.minute
//        } else {
//            timePicker.currentMinute
//        }
//
//        val selectedAmPm: String = if (selectedHour >= 12) {
//            "오후"
//        } else {
//            "오전"
//        }
//
////        val tvTime: TextView = findViewById(R.id.tv_calendar_detailview_modify2_time)
////        tvTime.text = ("$selectedAmPm $selectedHour:${selectedMinute}시")
//    }

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
