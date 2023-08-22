package com.umc.yourweather.presentation.calendardetailview

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.umc.yourweather.util.AlertDialogTwoBtn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class CalendarPlusWeather : AppCompatActivity(), CalendarDetailViewTimepicker.TimePickerListener {
    private lateinit var binding: ActivityCalendarPlusWeatherBinding
    private lateinit var editText: AppCompatEditText
    private var isSeekBarAdjusted = false // 변수 선언
    private var selectedStatus: Status? = null // 기본값으로 null 설정
    private var localDateTime: String = ""
    private var isTimePickerUsed = false // 타임피커를 사용한 적이 있는가
    private var formattedCurrentTime: String = "" // 현재 시간을 API로 보낼 포맷을 담을 변수

    override fun onTimeSelected(localDateTime: String) {
        this.localDateTime = localDateTime
        Log.d("TimePickerListener", "받은 localDateTime: $localDateTime")
    }

    override fun onResume() {
        super.onResume()
        // Update the time text with the current time
        val currentTime = getCurrentTime()
        updateTimeText(currentTime)
    }

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
        binding.btnCalendarDetailviewBack.setOnClickListener {
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
        Log.d("CalendarDetail 에서 넘어온 경우 날짜", "$unWrittenDate")

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
            isTimePickerUsed = true // Set the flag to true

            val fragmentManager = supportFragmentManager
            val timePicker = CalendarDetailViewTimepicker()
            val transaction = fragmentManager.beginTransaction()
            transaction.addToBackStack(null) // 프래그먼트를 백 스택에 추가
            transaction.replace(R.id.constraint, timePicker)
            transaction.commit()
        }

        // 메모 저장 API 전송
        // memoDate - CalendarDetail 화면에서 넘어온 경우 해당 날짜
        // localDateTime - 미입력 화면에서 넘어온 경우
        // ...

        binding.btnCalendardetailviewSave.setOnClickListener {
            val content: String? = editText.text?.toString()
            val temperature: Int? = binding.seekbarCalendarDetailviewTemp2.progress

            if (!isTimePickerUsed) {
                localDateTime = formattedCurrentTime // 타임피커로 따로 값 설정 안 한 경우
            }

            if (localDateTime.isBlank()) {
                // 시간이 입력되지 않은 경우
                Toast.makeText(this@CalendarPlusWeather, "시간을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // 메소드 종료
            }

            val finalLocalDateTime = if (unWrittenDate != null) {
                // 미입력에서 넘어온 경우 unWrittenDate와 localDateTime 합치기
                "$unWrittenDate$localDateTime"
            } else if (memoDate != null) {
                // 캘린더에서 넘어온 경우 memoDate와 localDateTime 합치기
                "$memoDate$localDateTime"
            } else {
                // 이외의 경우
                ""
            }

            if (finalLocalDateTime.isNotBlank()) {
                selectedStatus?.let { status ->
                    GlobalScope.launch(Dispatchers.IO) {
                        writeMemoAPI(status, content, finalLocalDateTime, temperature)
                    }
                }
            }
        }
    }

    // 현재 시간 업데이트 함수
    private fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val amPm = if (calendar.get(Calendar.AM_PM) == Calendar.AM) "오전" else "오후"

        // 현재 시간을 변환된 포맷으로 저장
        formattedCurrentTime = String.format("T%02d:%02d", hour % 12, minute)

        return String.format("%s %02d:%02d", amPm, hour % 12, minute)

    }

    // 뒤로가기 시 뜨는 다이얼로그
    private fun showMemoCancleDialog() {
        val alertDialog = AlertDialogTwoBtn(this)

        alertDialog.setTitle("감정날씨 입력을 취소하시겠어요?")
        alertDialog.setSubTitle("기록한 내용이 전부 사라집니다.")

        alertDialog.setNegativeButton("아니요") { dialogInterface, _ ->

            dialogInterface.dismiss()
        }

        alertDialog.setPositiveButton("네") { dialogInterface, _ ->
            dialogInterface.dismiss()

            finish()
        }

        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
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
        val buttonAnimation: Animation =
            AnimationUtils.loadAnimation(this, R.anim.btn_weather_scale)

        binding.btnHomeSun.clearAnimation()
        binding.btnHomeCloud.clearAnimation()
        binding.btnHomeThunder.clearAnimation()
        binding.btnHomeRain.clearAnimation()

        button.startAnimation(buttonAnimation)
    }

    // seekBar 리스너
    private fun setupSeekBarListener() {
        val seekBar = binding.seekbarCalendarDetailviewTemp2
        seekBar.progress = 0

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean,
            ) {
                isSeekBarAdjusted = fromUser
                updateSaveButtonState()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // 필요한 경우 구현
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                updateSaveButtonState()
                val seekbarValueTextView = binding.tvSeekbarValue

                val progress = seekBar?.progress ?: 0
                seekbarValueTextView.visibility = View.VISIBLE
                seekbarValueTextView.text = "$progress°"
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
    private fun writeMemoAPI(
        status: Status,
        content: String?,
        localDateTime: Any,
        temperature: Int?,
    ) {
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
                        Toast.makeText(
                            this@CalendarPlusWeather,
                            "기록이 저장되었습니다.",
                            Toast.LENGTH_SHORT,
                        ).show()

                        finish()
                    } else {
                        Log.d("메모 작성 실패", "메모 작성, 전달 실패: ${response.code()}")
                        Toast.makeText(
                            this@CalendarPlusWeather,
                            "기록이 저장이 되지 않았습니다. 다시 입력해주세요.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }

                override fun onFailure(call: Call<BaseResponse<MemoResponse>>, t: Throwable) {
                    Log.d("메모 작성 요청", "메모 작성 요청 실패: ${t.message}")
                }
            })
    }

    private fun activityFinish() {
        showMemoCancleDialog()
    }

    // 뒤로가기 누른 경우
    override fun onBackPressed() {
        showMemoCancleDialog()
    }
}
