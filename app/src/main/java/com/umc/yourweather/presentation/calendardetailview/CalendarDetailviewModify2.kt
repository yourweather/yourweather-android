package com.umc.yourweather.presentation.calendardetailview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalendarDetailviewModify2 : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarDetailviewModify2Binding
    private lateinit var editText: AppCompatEditText
    private var isSeekBarAdjusted = false // 변수 선언
    private var selectedStatus: Status? = null// 기본값으로 SUNNY 설정

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarDetailviewModify2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        editText = binding.editText as AppCompatEditText

        setupWeatherButtons()
        setupSeekBarListener()

        // 미입력에서 넘어오는 날짜
        val unWrittenDate = intent.getStringExtra("unWrittenDate")
        if (unWrittenDate != null) {
            val dateParts = unWrittenDate.split("-")
            if (dateParts.size == 3) {
                val month = dateParts[1].toInt()
                val day = dateParts[2].toInt()
                binding.tvDetailviewModify2Date.text = "${month}월 ${day}의 기록"
            }
        }

        binding.btnCalendardetailviewSave.setOnClickListener {
            val content: String? = editText.text?.toString()
            val localDateTime: String? = getCurrentDateTime()
            val temperature: Int? = binding.seekbarCalendarDetailviewTemp2.progress

            // writeMemo 함수 호출
            selectedStatus?.let { it1 -> writeMemo(it1, content, localDateTime, temperature) }
        }
        binding.flCalendarDetailviewBack.setOnClickListener {
            val intent = Intent(this, CalendarDetailView3::class.java)
            startActivity(intent)
            finish() // 현재 액티비티 종료
        }
    }

    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val now = Calendar.getInstance().time
        return dateFormat.format(now)
    }

    private fun setupWeatherButtons() {
        val buttonAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.btn_weather_scale)

        binding.btnHomeSun.setOnClickListener {
            selectedStatus = Status.SUNNY
            animateAndHandleButtonClick(binding.btnHomeSun)
            updateSaveButtonState()
        }

        binding.btnHomeCloud.setOnClickListener {
            selectedStatus = Status.CLOUDY
            animateAndHandleButtonClick(binding.btnHomeCloud)
            updateSaveButtonState()
        }

        binding.btnHomeThunder.setOnClickListener {
            selectedStatus = Status.LIGHTNING
            animateAndHandleButtonClick(binding.btnHomeThunder)
            updateSaveButtonState()
        }

        binding.btnHomeRain.setOnClickListener {
            selectedStatus = Status.RAINY
            animateAndHandleButtonClick(binding.btnHomeRain)
            updateSaveButtonState()
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
                updateSaveButtonState()
            }
        })
    }

    private fun updateSaveButtonState() {

        if (selectedStatus != null) {
            val isActive = isSeekBarAdjusted

            binding.btnCalendardetailviewSave.isEnabled = isActive
            binding.btnCalendardetailviewSave.setTextColor(
                ContextCompat.getColor(
                    this,
                    if (isActive) R.color.sorange else R.color.gray
                )
            )
        }
    }

    private fun writeMemo(status: Status, content: String?, localDateTime: String?, temperature: Int?) {
        val memoService = RetrofitImpl.authenticatedRetrofit.create(MemoService::class.java)
        memoService.memoWrite(MemoRequest(status, content, localDateTime, temperature))
            .enqueue(object : Callback<BaseResponse<MemoResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<MemoResponse>>,
                    response: Response<BaseResponse<MemoResponse>>
                ) {
                    if (response.isSuccessful) {
                        val memoResponse = response.body()?.result
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

    private fun showKeyboardAndFocusEditText() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        editText.requestFocus()
    }
}
