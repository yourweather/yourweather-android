package com.umc.yourweather.presentation.calendardetailview

import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.umc.yourweather.R
import com.umc.yourweather.data.enums.Status
import com.umc.yourweather.data.remote.request.MemoUpdateRequest
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.MemoUpdateResponse
import com.umc.yourweather.data.service.MemoService
import com.umc.yourweather.databinding.ActivityCalendarModifyWeatherBinding
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.di.UserSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalendarModifyWeatherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarModifyWeatherBinding
    private lateinit var editText: AppCompatEditText
    private var isSeekBarAdjusted = false // 변수 선언
    private var selectedStatus: Status? = null // 기본값으로 null 설정

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarModifyWeatherBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // 닉네임 적용
        val userNickname = UserSharedPreferences.getUserNickname(this)

        binding.tvDetailviewModify2Title1.text = "$userNickname 님의 감정 상태"
        binding.tvDetailviewModify2Title2.text = "$userNickname 님의 감정 온도"
        binding.tvDetailviewModify2Title3.text = "$userNickname 님의 일기"

        // 뒤로 가기 버튼
        binding.flCalendarDetailviewBack.setOnClickListener {
            finish()
        }
        // 디테일뷰에서 값 받아야댐
        // 화면에 보여줄 날짜 값ㅛ
        val modifyWrittenDate = intent.getStringExtra("modifyWrittenDate")
        if (modifyWrittenDate != null) {
            val dateParts = modifyWrittenDate.split("-")
            if (dateParts.size == 3) {
                val month = dateParts[1].toInt()
                val day = dateParts[2].toInt()
                binding.tvDetailviewModify2Date.text = "${month}월 ${day}의 기록"
            }
        }

        setupWeatherButtons()
        setupSeekBarListener()

        // 메모 Id 받기
        // Intent에서 캘린더에서 접근 시 memoId 추출
        val memoId = intent.getIntExtra("memoId", -1)
        // Intent에서 상세보기에서 접근 시 memoId 추출
        val memoIdW = intent.getIntExtra("memoIdW", -1)

        // 메모 content 입력창
        val content: String? = binding.editText.text?.toString()

        // 온도 값 seekBar에서 받기
        val temperature: Int? = binding.seekbarCalendarDetailviewTemp2.progress

        if (memoId != -1) {
            Log.d("캘린더에서 접근 메모 아이디(수정화면)", "$memoId")
            // 메모 수정 API 시작, 시작을 위한
            selectedStatus?.let { status ->
                GlobalScope.launch(Dispatchers.IO) {
                    temperature?.let { modifyMemoAPI(memoId, status, content, it) }
                }
            }
        } else if (memoIdW != -1) {
            Log.d("상세보기에서 접근 메모 아이디(수정화면)", "$memoIdW")
            // 메모 수정 API 시작, 시작을 위한
            selectedStatus?.let { status ->
                GlobalScope.launch(Dispatchers.IO) {
                    temperature?.let { modifyMemoAPI(memoIdW, status, content, it) }
                }
            }
        } else {
            Log.d("메모 아이디", "Invalid memoId and memoIdW values. Finishing activity.")
            finish()
        }
    }

    // 날씨 변경
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

    // seekBar 리스너
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

    // 메모 수정 API
    private fun modifyMemoAPI(memoId: Int, status: Status, content: String?, temperature: Int) {
        // Create a MemoUpdateRequest instance and populate it with the provided parameters
        val updatedMemo = content?.let {
            MemoUpdateRequest(
                status = status,
                content = it,
                temperature = temperature,
            )
        }

        val memoService = RetrofitImpl.authenticatedRetrofit.create(MemoService::class.java)
        updatedMemo?.let {
            memoService.memoUpdate(memoId, it)
                .enqueue(object : Callback<BaseResponse<MemoUpdateResponse>> {
                    override fun onResponse(
                        call: Call<BaseResponse<MemoUpdateResponse>>,
                        response: Response<BaseResponse<MemoUpdateResponse>>,
                    ) {
                        if (response.isSuccessful) {
                            val memoResponse = response.body()?.result
                            Log.d("메모 수정", "메모 수정 성공 ${response.body()?.result}")
                            finish()
                        } else {
                            Log.d("메모 수정 실패", "메모 수정 실패: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<BaseResponse<MemoUpdateResponse>>, t: Throwable) {
                        Log.d("메모 수정 요청", "메모 수정 요청 실패: ${t.message}")
                    }
                })
        }
    }

    // 뒤로 가기 누른 경우
    override fun onBackPressed() {
        finish()
    }
}
