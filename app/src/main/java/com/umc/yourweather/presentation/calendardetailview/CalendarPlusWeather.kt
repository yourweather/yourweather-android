package com.umc.yourweather.presentation.calendardetailview

import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.umc.yourweather.R
import com.umc.yourweather.data.enums.Status
import com.umc.yourweather.data.remote.request.MemoRequest
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.MemoResponse
import com.umc.yourweather.data.service.MemoService
import com.umc.yourweather.databinding.ActivityCalendarPlusWeatherBinding
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.di.UserSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalendarPlusWeather : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarPlusWeatherBinding
    private lateinit var editText: AppCompatEditText
    private var isSeekBarAdjusted = false // 변수 선언
    private var selectedStatus: Status? = null // 기본값으로 null 설정

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarPlusWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editText = binding.editText as AppCompatEditText

        setupWeatherButtons()
        setupSeekBarListener()

        binding.flCalendarDetailviewBack.setOnClickListener {
            activityFinish()
        }
        val userNickname = UserSharedPreferences.getUserNickname(this@CalendarPlusWeather)

        binding.tvDetailviewModify2Title1.text = "$userNickname 님의 감정 상태"
        binding.tvDetailviewModify2Title2.text = "$userNickname 님의 감정 온도"
        binding.tvDetailviewModify2Title3.text = "$userNickname 님의 일기"

        // CalendarDetail 에서 넘어온 경우 날짜
        val memoDate = intent.getStringExtra("date")
        Log.d("CalendarDetail 에서 넘어온 경우 날짜", "$memoDate")
        if (memoDate != null) {
            val dateParts = memoDate.split("-")
            if (dateParts.size == 3) {
                val month = dateParts[1].toInt()
                val day = dateParts[2].toInt()
                binding.tvDetailviewModify2Date.text = "${month}월 ${day}의 기록"
                Log.d("CalendarDetail 에서 넘어온 경우 날짜 파싱", "${month}월 ${day}의 기록")
            }
        }

        // 미입력에서 넘어온 경우 날짜
        val unWrittenDate = intent.getStringExtra("unWrittenDate")
        Log.d("CalendarDetail 에서 넘어온 경우 날짜", "$memoDate")

        if (unWrittenDate != null) {
            val dateParts = unWrittenDate.split("-")
            if (dateParts.size == 3) {
                val month = dateParts[1].toInt()
                val day = dateParts[2].toInt()
                binding.tvDetailviewModify2Date.text = "${month}월 ${day}의 기록"
            }
        }

        // 타임피커 시간 관련 코드
        binding.tvDetailviewModify2Time.setOnClickListener {
            val fragmentManager = supportFragmentManager
            val timePicker = CalendarDetailViewTimepicker()
            val transaction = fragmentManager.beginTransaction()
            transaction.addToBackStack(null) // 프래그먼트를 백 스택에 추가
            transaction.replace(R.id.fragment_container, timePicker)
            transaction.commit()
        }

        // 메모 저장 API 전송
        // memoDate - CalendarDetail 화면에서 넘어온 경우 해당 날짜
        // localDateTime - 미입력 화면에서 넘어온 경우
        binding.btnCalendardetailviewSave.setOnClickListener {
            val content: String? = editText.text?.toString()
            val temperature: Int? = binding.seekbarCalendarDetailviewTemp2.progress
            // 타임피커로 부터 날짜, 시간 전달 받기 2023-08-19T17:48 꼴
            val selectedTime = intent.getStringExtra("selectedTime")

            selectedStatus?.let { status ->
                GlobalScope.launch(Dispatchers.IO) {
                    writeMemoAPI(status, content, selectedTime, temperature)
                }
            }
        }
    }

    // 사용자가 입력한 시간 값
    fun updateTimeText(text: String) {
        binding.tvDetailviewModify2Time.text = text
    }

    // 아이콘 클릭 시 해당 값 반환
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

    // 아이콘 클릭 시 애니메이션
    private fun animateAndHandleButtonClick(button: Button) {
        val buttonAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.btn_weather_scale)

        binding.btnHomeSun.clearAnimation()
        binding.btnHomeCloud.clearAnimation()
        binding.btnHomeThunder.clearAnimation()
        binding.btnHomeRain.clearAnimation()

        button.startAnimation(buttonAnimation)
    }

    // SeekBar 리스너
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

    // 입력 내용이 있을 경우 저장 버튼 활성화
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

    // 메모 작성 API
    private fun writeMemoAPI(status: Status, content: String?, localDateTime: String?, temperature: Int?) {
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
                        Toast.makeText(this@CalendarPlusWeather, "메모가 저장되었습니다.", Toast.LENGTH_SHORT).show()

                        activityFinish()
                    } else {
                        Log.d("메모 작성 실패", "메모 작성, 전달 실패: ${response.code()}")
                        Toast.makeText(this@CalendarPlusWeather, "메모가 저장이 되지 않았습니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<BaseResponse<MemoResponse>>, t: Throwable) {
                    Log.d("메모 작성 요청", "메모 작성 요청 실패: ${t.message}")
                }
            })
    }

    private fun activityFinish() {
        finish()
    }

    // 뒤로가기 누른 경우
    override fun onBackPressed() {
        finish()
    }
}
