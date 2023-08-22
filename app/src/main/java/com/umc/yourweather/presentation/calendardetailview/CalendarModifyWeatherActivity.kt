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
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContentProviderCompat.requireContext
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
import com.umc.yourweather.util.AlertDialogTwoBtn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class CalendarModifyWeatherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarModifyWeatherBinding
    private lateinit var editText: AppCompatEditText
    private var isSeekBarAdjusted = false // 변수 선언

    // 메모 초기값
    private var selectedStatus: Status? = null
    private var initialTemperature: Int = 0
    private var initialContent: String? = null
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
            showMemoCancleDialog()
        }
        binding.btnCalendarDetailviewBack.setOnClickListener {
            showMemoCancleDialog()
        }

        val modifyStatus = intent.getSerializableExtra("modifyStatus") as Status
        val modifyTemperature = intent.getIntExtra("modifyTemperature", 0)
        val modifyContent = intent.getStringExtra("modifyContent")
        val modifyDateTime = intent.getStringExtra("modifyDateTime")
        Log.d("수정 액티비티로 넘겨받는 값 확인", " $modifyStatus, $modifyContent, $modifyDateTime, $modifyTemperature")

        // 받은 값을 뷰에 보여지도록 변수에 값 할당
        selectedStatus = modifyStatus
        initialTemperature = modifyTemperature
        initialContent = modifyContent
        Log.d("초기값 확인", " $selectedStatus, $initialTemperature, $initialContent")

        animateAndHandleInitial(selectedStatus!!)
        binding.editText.setText(initialContent)
        setupSeekBarListener(initialTemperature)
        binding.seekbarCalendarDetailviewTemp2.progress = initialTemperature
        binding.tvSeekbarValue.text = "$initialTemperature°"
        setupWeatherButtons()
        if (modifyDateTime != null) {
            formatDateTime(modifyDateTime)
        }

        // 저장버튼 클릭 시
        binding.btnCalendardetailviewSave.setOnClickListener {
            val content: String? = binding.editText.text?.toString()
            val temperature: Int? = binding.seekbarCalendarDetailviewTemp2.progress

            if (selectedStatus != null && content != null && temperature != null) {
                val memoId = intent.getIntExtra("memoId", -1)
                val memoIdW = intent.getIntExtra("memoIdW", -1)

                if (memoId != -1) {
                    Log.d("저장 버튼 클릭(수정화면)", "memoId: $memoId, content: $content, temperature: $temperature")
                    selectedStatus?.let { status ->
                        GlobalScope.launch(Dispatchers.IO) {
                            modifyMemoAPI(memoId, status, content, temperature)
                        }
                    }
                } else if (memoIdW != -1) {
                    Log.d("저장 버튼 클릭(수정화면)", "memoIdW: $memoIdW, content: $content, temperature: $temperature")
                    selectedStatus?.let { status ->
                        GlobalScope.launch(Dispatchers.IO) {
                            modifyMemoAPI(memoIdW, status, content, temperature)
                        }
                    }
                } else {
                    Log.d("메모 아이디", "Invalid memoId and memoIdW values. Cannot proceed.")
                }
            }
        }


    }

    // 특정 메모 시간 포맷
    fun formatDateTime(inputDateTime: String) {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("M월 d일 a h:mm", Locale.getDefault())

        val date = inputFormat.parse(inputDateTime)
        binding.tvDetailviewModify2Date.text = outputFormat.format(date)
        Log.d("포맷함수 확인", "${outputFormat.format(date)}")
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

    private fun animateAndHandleInitial(status: Status) {
        val buttonAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.btn_weather_scale)

        binding.btnHomeSun.clearAnimation()
        binding.btnHomeCloud.clearAnimation()
        binding.btnHomeThunder.clearAnimation()
        binding.btnHomeRain.clearAnimation()

        when (status) {
            Status.SUNNY -> binding.btnHomeSun.startAnimation(buttonAnimation)
            Status.CLOUDY -> binding.btnHomeCloud.startAnimation(buttonAnimation)
            Status.LIGHTNING -> binding.btnHomeThunder.startAnimation(buttonAnimation)
            Status.RAINY -> binding.btnHomeRain.startAnimation(buttonAnimation)
        }
    }

    private fun updateSaveButtonState() {
        if (selectedStatus != null) {
            binding.btnCalendardetailviewSave.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.sorange,
                ),
            )
        }
    }

    // seekBar 리스너
    private fun setupSeekBarListener(temperature: Int) {
        val seekBar = binding.seekbarCalendarDetailviewTemp2
        seekBar.progress = temperature

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
                val seekbarValueTextView = binding.tvSeekbarValue

                val progress = seekBar?.progress ?: 0
                seekbarValueTextView.visibility = View.VISIBLE
                seekbarValueTextView.text = "$progress°"
            }
        })
    }

    // 뒤로가기 시 뜨는 다이얼로그
    private fun showMemoCancleDialog() {
        val alertDialog = AlertDialogTwoBtn(this)

        alertDialog.setTitle("감정날씨 입력을 취소하시겠어요?")

        alertDialog.setNegativeButton("아니요") { dialogInterface, _ ->
            dialogInterface.dismiss()

            dialogInterface.dismiss()
        }

        alertDialog.setPositiveButton("네") { dialogInterface, _ ->
            dialogInterface.dismiss()

            finish()
        }

        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
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
                            finish() // 수정 성공 시 화면 종료
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
        showMemoCancleDialog()
    }
}
