package com.umc.yourweather.presentation.calendardetailview

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.mmk.timeintervalpicker.TimeIntervalPicker
import com.umc.yourweather.R
import com.umc.yourweather.data.enums.Status
import com.umc.yourweather.data.remote.request.MemoRequest
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.MemoResponse
import com.umc.yourweather.data.service.MemoService
import com.umc.yourweather.databinding.ActivityCalendarDetailviewModify2Binding
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.di.UserSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
    private var selectedStatus: Status? = null // 기본값으로 SUNNY 설정

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarDetailviewModify2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        editText = binding.editText as AppCompatEditText

        setupWeatherButtons()
        setupSeekBarListener()

        val unWrittenDate = intent.getStringExtra("unWrittenDate")
        if (unWrittenDate != null) {
            val dateParts = unWrittenDate.split("-")
            if (dateParts.size == 3) {
                val month = dateParts[1].toInt()
                val day = dateParts[2].toInt()
                binding.tvDetailviewModify2Date.text = "${month}월 ${day}의 기록"
            }
        }

        binding.flCalendarDetailviewBack.setOnClickListener {
            activityFinish()
        }
        val userNickname = UserSharedPreferences.getUserNickname(this@CalendarDetailviewModify2)

        binding.tvDetailviewModify2Title1.text = "$userNickname 님의 감정 상태"
        binding.tvDetailviewModify2Title2.text = "$userNickname 님의 감정 온도"
        binding.tvDetailviewModify2Title3.text = "$userNickname 님의 일기"

        val timeIntervalPicker = TimeIntervalPicker.Builder()
            .setTitleText("알림 시간 설정")
            .setIntervalBetweenHours(1)
            .setIntervalBetweenMinutes(5)
            .setHour(12)
            .setMinute(0)
            .setHourListCircular(true)
            .setMinuteListCircular(true)
            .build()

        timeIntervalPicker.addOnPositiveButtonClickListener {
            val formattedTime = formatTimeWithAmPm(timeIntervalPicker.hour, timeIntervalPicker.minute)
            binding.tvDetailviewModify2Time.text = formattedTime
        }
        binding.tvDetailviewModify2Time.setOnClickListener {
            timeIntervalPicker.show(supportFragmentManager, "TimeIntervalPicker")
        }
        binding.btnCalendardetailviewSave.setOnClickListener {
            val content: String? = editText.text?.toString()
            val localDateTime: String? = unWrittenDate?.let {
                combineDateAndTime(it, binding.tvDetailviewModify2Time.text.toString())
            }
            val temperature: Int? = binding.seekbarCalendarDetailviewTemp2.progress

            selectedStatus?.let { status ->
                GlobalScope.launch(Dispatchers.IO) {
                    val formattedLocalDateTime = formatLocalDateTime(localDateTime)
                    writeMemo(status, content, formattedLocalDateTime, temperature)
                }
            }
        }

    }
    private fun formatLocalDateTime(localDateTime: String?): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd a hh:mm", Locale.getDefault())
        val date = inputFormat.parse(localDateTime)
        val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        return outputFormat.format(date)
    }

    private fun formatTimeWithAmPm(hour: Int, minute: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)

        val simpleDateFormat = SimpleDateFormat("a hh:mm", Locale.getDefault())
        return simpleDateFormat.format(calendar.time)
    }

    private fun combineDateAndTime(date: String, time: String): String {
        val combinedDateTime = "$date $time"
        val desiredFormat = SimpleDateFormat("yyyy-MM-dd a hh:mm", Locale.getDefault())
        return desiredFormat.format(desiredFormat.parse(combinedDateTime))
    }

    // 뒤로 가기 누른 경우
    override fun onBackPressed() {
        finish()
    }

    private fun setupWeatherButtons() {
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
                    if (isActive) R.color.sorange else R.color.gray,
                ),
            )
        }
    }

    private fun writeMemo(status: Status, content: String?, localDateTime: String?, temperature: Int?) {
        val memoService = RetrofitImpl.authenticatedRetrofit.create(MemoService::class.java)
        memoService.memoWrite(MemoRequest(status, content, localDateTime, temperature))
            .enqueue(object : Callback<BaseResponse<MemoResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<MemoResponse>>,
                    response: Response<BaseResponse<MemoResponse>>,
                ) {
                    if (response.isSuccessful) {
                        val memoResponse = response.body()?.result
                        Log.d("메모 작성", "메모 작성, 전달 성공 ${response.body()?.result}")
                        activityFinish()
                    } else {
                        Log.d("메모 작성 실패", "메모 작성, 전달 성공 실패: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<BaseResponse<MemoResponse>>, t: Throwable) {
                    Log.d("메모 작성 요청", "메모 작성 요청 실패: ${t.message}")
                }
            })
    }

    private fun activityFinish() {
        val intent = Intent(this, CalendarDetailView3::class.java)
        startActivity(intent)
        finish()
    }
}
