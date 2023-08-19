package com.umc.yourweather.presentation.calendardetailview

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.R
import com.umc.yourweather.data.enums.Status
import com.umc.yourweather.data.remote.request.MemoUpdateRequest
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.MemoResponse
import com.umc.yourweather.data.remote.response.MemoUpdateResponse
import com.umc.yourweather.data.service.MemoService
import com.umc.yourweather.databinding.ActivityCalendarWeatherDetailBinding
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.di.UserSharedPreferences
import com.umc.yourweather.presentation.calendar.CalendarDetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class CalendarWeatherDetail : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarWeatherDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarWeatherDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent에서 캘린더에서 접근 시 memoId 추ㅛㅕㅕ갸출
        val memoId = intent.getIntExtra("memoId", -1)
        // Intent에서 상세보기에서 접근 시 memoId 추출
        val memoIdW = intent.getIntExtra("memoIdW", -1)

        if (memoId != -1) {
            Log.d("캘린더에서 접근 메모 아이디", "$memoId")
            detailMemoReturnApi(memoId)
        } else if (memoIdW != -1) {
            Log.d("상세보기에서 접근 메모 아이디", "$memoIdW")
            detailMemoReturnApi(memoIdW)
        } else {
            Log.d("메모 아이디", "Invalid memoId and memoIdW values. Finishing activity.")
            finish()
        }

        // 닉네임
        val userNickname = UserSharedPreferences.getUserNickname(this)
        binding.tvDetailviewModify2Title1.text = "$userNickname 님의 감정 상태"
        binding.tvDetailviewModify2Title2.text = "$userNickname 님의 감정 온도"
        binding.tvDetailviewModify2Title3.text = "$userNickname 님의 일기"

        // 수정 프래그먼트 파일 띄우기
        binding.btnCalendardetailviewModify.setOnClickListener {
            val intent = Intent(this, CalendarModifyWeatherActivity::class.java)
            startActivity(intent)
        }

        binding.flCalendarDetailviewBack.setOnClickListener {
            val intent = Intent(this, CalendarDetail::class.java)
            startActivity(intent)
        }
    }

    // 특정 메모 반환
    private fun detailMemoReturnApi(memoId: Int) {
        val service = RetrofitImpl.authenticatedRetrofit.create(MemoService::class.java)
        val call = service.detailMemoReturn(memoId = memoId)

        call.enqueue(object : Callback<BaseResponse<MemoResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<MemoResponse>>,
                response: Response<BaseResponse<MemoResponse>>,
            ) {
                if (response.isSuccessful) {
                    val MemoResponse = response.body()?.result
                    if (MemoResponse != null) {
                        val status = MemoResponse.status
                        val temperature = MemoResponse.temperature
                        val content = MemoResponse.content
                        val weatherId = MemoResponse.weatherId
                        val dateTime = MemoResponse.localDateTime

                        // 메모 내용
                        binding.editText.text = content
                        // 메모 온도에 따른 seekBar
                        if (temperature != null) {
                            setupSeekBarListener(temperature)
                        }
                        // 날씨 애니메이션 적용
                        animateAndHandleButtonClick(status)
                        // 날짜 시간
                        if (dateTime != null) {
                            formatDateTime(dateTime)
                        }
                    } else {
                        Log.e("특정 메모 반환 API Error", "Response body 비었음")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("특정 메모 반환 API Failure", "Response Code: ${response.code()}, Error Body: $errorBody")
                }
            }

            override fun onFailure(call: Call<BaseResponse<MemoResponse>>, t: Throwable) {
                Log.e("특정 메모 반환 API Failure", "Error: ${t.message}", t)
            }
        })
    }

    // 특정 메모 시간 포맷
    fun formatDateTime(inputDateTime: String) {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("M월 d일 a h:mm", Locale.getDefault())

        val date = inputFormat.parse(inputDateTime)
        binding.tvDetailviewModify2Date.text = outputFormat.format(date)
    }

    // seekBar 업데이트
    private fun setupSeekBarListener(temperature: Int) {
        val seekBar = binding.seekbarCalendarDetailviewTemp2

        // 설정한 온도값에 맞춰 SeekBar의 상태를 변경
        seekBar.progress = temperature

        // 터치 이벤트 비활성화
        seekBar.setOnTouchListener { _, _ -> true }
    }

    // 날씨 상태 애니메이션
    private fun animateAndHandleButtonClick(Status: Status) {
        val buttonAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.btn_weather_scale)
        when (Status) {
            com.umc.yourweather.data.enums.Status.SUNNY -> binding.btnHomeSun.startAnimation(buttonAnimation)
            com.umc.yourweather.data.enums.Status.CLOUDY -> binding.btnHomeCloud.startAnimation(buttonAnimation)
            com.umc.yourweather.data.enums.Status.RAINY -> binding.btnHomeRain.startAnimation(buttonAnimation)
            com.umc.yourweather.data.enums.Status.LIGHTNING -> binding.btnHomeThunder.startAnimation(buttonAnimation)
        }
    }

    // 메모 삭제 API
    private fun deleteMemo(memoIdToDelete: Int) {
        val memoService = RetrofitImpl.authenticatedRetrofit.create(MemoService::class.java)
        memoService.memoDelete(memoIdToDelete).enqueue(object : Callback<BaseResponse<Unit>> {
            override fun onResponse(
                call: Call<BaseResponse<Unit>>,
                response: Response<BaseResponse<Unit>>,
            ) {
                if (response.isSuccessful) {
                    // Memo deletion was successful
                    Log.d("메모 삭제", "메모 삭제, 삭제 전달 성공 ${response.body()?.result}")

                    val baseResponse = response.body()
                    // Handle your success scenario here
                    Log.d("메모 삭제", "메모 삭제, 삭제 전달 실패 ${response.body()?.result}")
                } else {
                    // Memo deletion failed
                    Log.d("메모 삭제 응답 실패", "메모 삭제, 응답 실패 ${response.body()?.result}")
                }
            }

            override fun onFailure(call: Call<BaseResponse<Unit>>, t: Throwable) {
                // Handle failure (network issue, etc.)
            }
        })
    }

    // 메모 수정 API
    private fun updateMemo(memoId: Int, status: Status, temperature: Int, content: String) {
        val memoService = RetrofitImpl.authenticatedRetrofit.create(MemoService::class.java)

        val updateRequest = MemoUpdateRequest(status, temperature, content)

        memoService.memoUpdate(memoId, updateRequest).enqueue(object :
            Callback<BaseResponse<MemoUpdateResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<MemoUpdateResponse>>,
                response: Response<BaseResponse<MemoUpdateResponse>>,
            ) {
                if (response.isSuccessful) {
                    // Memo update was successful
                    val baseResponse = response.body()
                    Log.d("메모 수정", "메모 수정, 수정 전달 성공 ${response.body()?.result}")
                } else {
                    Log.d("메모 수정", "메모 수정 전달 실패 ${response.body()?.result}")
                    val errorResponse = response.errorBody()?.string()
                    // Handle your error scenario here
                }
            }

            override fun onFailure(call: Call<BaseResponse<MemoUpdateResponse>>, t: Throwable) {
                // Handle failure (network issue, etc.)
            }
        })
    }
}
