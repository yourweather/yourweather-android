package com.umc.yourweather.presentation.calendardetailview

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class CalendarWeatherDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarWeatherDetailBinding

    private lateinit var modifyStatus: Status
    private var modifyTemperature: Int = 0
    private lateinit var modifyContent: String
    private lateinit var modifyDateTime: String

    override fun onResume() {
        super.onResume()

        // 다시 화면이 보여질 때 메모 정보를 갱신
        val memoId = intent.getIntExtra("memoId", -1)
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarWeatherDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("현재 뷰", "CalendarWeatherDetail")
        // Intent에서 캘린더에서 접근 시 memoId 추출
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
            // Intent에서 캘린더에서 접근 시 memoId 추출
            val memoId = intent.getIntExtra("memoId", -1)
            // Intent에서 상세보기에서 접근 시 memoId 추출
            val memoIdW = intent.getIntExtra("memoIdW", -1)

            if (memoId != -1) {
                val intent = Intent(this, CalendarModifyWeatherActivity::class.java)
                intent.putExtra("memoId", memoId)
                intent.putExtra("modifyStatus", modifyStatus)
                intent.putExtra("modifyTemperature", modifyTemperature)
                intent.putExtra("modifyContent", modifyContent)
                intent.putExtra("modifyDateTime", modifyDateTime)

                Log.d("수정 액티비티로 넘겨주는 값 확인", "$memoId, $modifyStatus, $modifyContent, $modifyDateTime, $modifyTemperature")
                startActivity(intent)
            } else if (memoIdW != -1) {
                val intent = Intent(this, CalendarModifyWeatherActivity::class.java)
                intent.putExtra("memoIdW", memoIdW)
                intent.putExtra("modifyStatus", modifyStatus)
                intent.putExtra("modifyTemperature", modifyTemperature)
                intent.putExtra("modifyContent", modifyContent)
                intent.putExtra("modifyDateTime", modifyDateTime)
                Log.d("수정 액티비티로 넘겨주는 값 확인", "$memoIdW, $modifyStatus, $modifyContent, $modifyDateTime, $modifyTemperature")

                startActivity(intent)
            } else {
                Log.d("메모 아이디", "Invalid memoId and memoIdW values. Cannot proceed.")
            }
        }

        binding.flCalendarDetailviewBack.setOnClickListener {
            finish()
            // 프래그먼트로 이동
        }

        // 메모 삭제 버튼 -> API 호출
        binding.btnCalendarWeatherDetailDelete.setOnClickListener {
            // Intent에서 캘린더에서 접근 시 memoId 추출
            val memoId = intent.getIntExtra("memoId", -1)
            // Intent에서 상세보기에서 접근 시 memoId 추출
            val memoIdW = intent.getIntExtra("memoIdW", -1)

            if (memoId != -1) {
                deleteMemoAPI(memoId)
                Log.d("메모 아이디 삭제 요청 - 캘린더 접근 메모", "$memoId")
            } else if (memoIdW != -1) {
                deleteMemoAPI(memoIdW)
                Log.d("메모 아이디 삭제 요청 - 상세보기 접근 메모", "$memoIdW")
            } else {
                Log.d("메모 아이디", "Invalid memoId and memoIdW values. Cannot proceed.")
            }
        }
    }

    // 메모 삭제 API
    private fun deleteMemoAPI(memoId: Int) {
        val service = RetrofitImpl.authenticatedRetrofit.create(MemoService::class.java)
        val call = service.memoDelete(memoId)

        call.enqueue(object : Callback<BaseResponse<Unit>> {
            override fun onResponse(
                call: Call<BaseResponse<Unit>>,
                response: Response<BaseResponse<Unit>>,
            ) {
                if (response.isSuccessful) {
                    // 메모 삭제 성공 처리
                    Log.d("메모 삭제 API", "메모가 성공적으로 삭제되었습니다.")

                    Toast.makeText(this@CalendarWeatherDetailActivity, "기록이 삭제되었습니다.", Toast.LENGTH_SHORT).show()

                    finish()

                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("메모 삭제 API Failure", "Response Code: ${response.code()}, Error Body: $errorBody")
                }
            }

            override fun onFailure(call: Call<BaseResponse<Unit>>, t: Throwable) {
                Log.e("메모 삭제 API Failure", "Error: ${t.message}", t)
            }
        })
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

                        modifyStatus = status
                        if (temperature != null) {
                            modifyTemperature = temperature
                        }
                        modifyContent = content
                        if (dateTime != null) {
                            modifyDateTime = dateTime
                        }

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
        binding.tvSeekbarValue.text = "$temperature°"

        // 터치 이벤트 비활성화
        seekBar.setOnTouchListener { _, _ -> true }
    }

    // 날씨 상태 애니메이션
    private fun animateAndHandleButtonClick(Status: Status) {
        // 애니메이션 중단
        binding.btnHomeSun.clearAnimation()
        binding.btnHomeCloud.clearAnimation()
        binding.btnHomeRain.clearAnimation()
        binding.btnHomeThunder.clearAnimation()

        val buttonAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.btn_weather_scale)
        when (Status) {
            com.umc.yourweather.data.enums.Status.SUNNY -> binding.btnHomeSun.startAnimation(buttonAnimation)
            com.umc.yourweather.data.enums.Status.CLOUDY -> binding.btnHomeCloud.startAnimation(buttonAnimation)
            com.umc.yourweather.data.enums.Status.RAINY -> binding.btnHomeRain.startAnimation(buttonAnimation)
            com.umc.yourweather.data.enums.Status.LIGHTNING -> binding.btnHomeThunder.startAnimation(buttonAnimation)
        }
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
                    Toast.makeText(this@CalendarWeatherDetailActivity, "메모가 수정되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("메모 수정", "메모 수정 전달 실패 ${response.body()?.result}")
                    val errorResponse = response.errorBody()?.string()
                    Toast.makeText(this@CalendarWeatherDetailActivity, "메모가 수정이 되지 않았습니다. 다시 실행해주세요.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BaseResponse<MemoUpdateResponse>>, t: Throwable) {
                // Handle failure (network issue, etc.)
            }
        })
    }
}
