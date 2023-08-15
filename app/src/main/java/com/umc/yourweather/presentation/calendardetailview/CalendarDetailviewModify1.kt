package com.umc.yourweather.presentation.calendardetailview

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatEditText
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.umc.yourweather.R
import com.umc.yourweather.data.enums.Status
import com.umc.yourweather.data.remote.request.MemoUpdateRequest
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.MemoUpdateResponse
import com.umc.yourweather.data.service.MemoService
import com.umc.yourweather.databinding.ActivityCalendarDetailviewModify1Binding
import com.umc.yourweather.databinding.ActivityCalendarDetailviewModify2Binding
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.presentation.weatherinput.HomeFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class CalendarDetailviewModify1 : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarDetailviewModify1Binding
    private lateinit var cardView: CardView
    private lateinit var editText: AppCompatEditText
    private var isSeekBarAdjusted = false // 변수 선언

    interface CalendarDetailviewModify1Listener {
        fun onWeatherButtonClicked(weatherType: String)
    }

    private var listener: CalendarDetailviewModify1Listener?=null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarDetailviewModify1Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val seekBar = binding.seekbarCalendarDetailviewTemp

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

        cardView = binding.cvCalendarDetailviewModify1
        editText = binding.etCalendarDetailviewModify1 as AppCompatEditText

        cardView.setOnClickListener {
            showKeyboardAndFocusEditText()
        }

        // 프래그먼트를 추가하고 초기화
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        // 프래그먼트 객체 생성
        val fragment1: Fragment = ModifyFragment2()

        // 프래그먼트를 레이아웃 컨테이너에 추가
        fragmentTransaction.add(R.id.fragment_container, fragment1)

        // 프래그먼트 트랜잭션 완료
        fragmentTransaction.commit()

        // 애니메이션 리소스 가져오기
        val buttonAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.btn_weather_scale)
        val buttonInFragment = fragment1.view?.findViewById<Button>(R.id.btn_calendardetailview_save)

        // 각 버튼과 애니메이션 연결
        binding.btnCalendarDetailviewModify1Sun.setOnClickListener {
            binding.btnCalendarDetailviewModify1Cloud.clearAnimation()
            binding.btnCalendarDetailviewModify1Thunder.clearAnimation()
            binding.btnCalendarDetailviewModify1Rain.clearAnimation()

            it.startAnimation(buttonAnimation)
            // 프래그먼트의 버튼 참조
            val buttonInFragment = fragment1.view?.findViewById<Button>(R.id.btn_calendardetailview_save)
            // 버튼의 텍스트 색상 변경
            buttonInFragment?.setTextColor(ContextCompat.getColor(this, R.color.sorange))
            updateStatus(Status.SUNNY) // 선택한 버튼에 따라 상태 갱신
        }

        binding.btnCalendarDetailviewModify1Cloud.setOnClickListener {
            binding.btnCalendarDetailviewModify1Sun.clearAnimation()
            binding.btnCalendarDetailviewModify1Thunder.clearAnimation()
            binding.btnCalendarDetailviewModify1Rain.clearAnimation()

            it.startAnimation(buttonAnimation)
            // 프래그먼트의 버튼 참조
            val buttonInFragment = fragment1.view?.findViewById<Button>(R.id.btn_calendardetailview_save)
            // 버튼의 텍스트 색상 변경
            buttonInFragment?.setTextColor(ContextCompat.getColor(this, R.color.sorange))
            updateStatus(Status.SUNNY) // 선택한 버튼에 따라 상태 갱신
        }

        binding.btnCalendarDetailviewModify1Thunder.setOnClickListener {
            binding.btnCalendarDetailviewModify1Sun.clearAnimation()
            binding.btnCalendarDetailviewModify1Cloud.clearAnimation()
            binding.btnCalendarDetailviewModify1Rain.clearAnimation()

            it.startAnimation(buttonAnimation)

            // 프래그먼트의 버튼 참조
            val buttonInFragment = fragment1.view?.findViewById<Button>(R.id.btn_calendardetailview_save)
            // 버튼의 텍스트 색상 변경
            buttonInFragment?.setTextColor(ContextCompat.getColor(this, R.color.sorange))
            updateStatus(Status.SUNNY) // 선택한 버튼에 따라 상태 갱신
        }

        binding.btnCalendarDetailviewModify1Rain.setOnClickListener {
            binding.btnCalendarDetailviewModify1Sun.clearAnimation()
            binding.btnCalendarDetailviewModify1Cloud.clearAnimation()
            binding.btnCalendarDetailviewModify1Thunder.clearAnimation()

            it.startAnimation(buttonAnimation)

            // 프래그먼트의 버튼 참조
            val buttonInFragment = fragment1.view?.findViewById<Button>(R.id.btn_calendardetailview_save)
            // 버튼의 텍스트 색상 변경
            buttonInFragment?.setTextColor(ContextCompat.getColor(this, R.color.sorange))
            updateStatus(Status.SUNNY) // 선택한 버튼에 따라 상태 갱신
        }

// 오렌지로 텍스트 색상이 변경되었을 때만 다른 화면으로 전환
        if (buttonInFragment?.currentTextColor == ContextCompat.getColor(this, R.color.sorange)) {

            buttonInFragment.isEnabled=true
            startActivity(intent)
            updateUserInputsAndCallApi()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateSaveButtonState() {
        val fragment1: Fragment? = supportFragmentManager.findFragmentById(R.id.fragment_container)
        val buttonInFragment = fragment1?.view?.findViewById<Button>(R.id.btn_calendardetailview_save)


        val isActive = isSeekBarAdjusted

        buttonInFragment?.isEnabled = isActive
        if (isActive) {
            buttonInFragment?.setTextColor(ContextCompat.getColor(this, R.color.sorange))
            //  val intent = Intent(this, AnotherActivity::class.java)
            startActivity(intent)
            updateUserInputsAndCallApi()
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
    private fun updateStatus(status: Status) {
        binding.btnCalendarDetailviewModify1Sun.isSelected = status == Status.SUNNY
        binding.btnCalendarDetailviewModify1Cloud.isSelected = status == Status.CLOUDY
        binding.btnCalendarDetailviewModify1Thunder.isSelected = status == Status.LIGHTNING
        binding.btnCalendarDetailviewModify1Rain.isSelected = status == Status.RAINY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateUserInputsAndCallApi() {
        val status = when {
            binding.btnCalendarDetailviewModify1Sun.isSelected -> Status.SUNNY
            binding.btnCalendarDetailviewModify1Cloud.isSelected -> Status.CLOUDY
            binding.btnCalendarDetailviewModify1Rain.isSelected -> Status.RAINY
            binding.btnCalendarDetailviewModify1Thunder.isSelected -> Status.LIGHTNING
            else -> Status.SUNNY // 기본값 설정
        }

        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)

        val temperature = binding.seekbarCalendarDetailviewTemp.progress // SeekBar 등에서 입력 받음

        val content = binding.etCalendarDetailviewModify1.text.toString().takeIf { it.isNotBlank() } ?: "" // 메모가 비어있다면 빈 문자열로 처리

        val updateRequest = MemoUpdateRequest(
            status = status,
            temperature = temperature,
            content = content,
        )

        // CalendarDetailviewModify1Api(memoId = ,updateRequest)
    }
    private fun CalendarDetailviewModify1Api(memoId: Int, request: MemoUpdateRequest){
        val memoService = RetrofitImpl.authenticatedRetrofit.create(MemoService::class.java)

        val call = memoService.memoUpdate(memoId, request)

        call.enqueue(object : retrofit2.Callback<BaseResponse<MemoUpdateResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<MemoUpdateResponse>>,
                response: retrofit2.Response<BaseResponse<MemoUpdateResponse>>
            ){
                if (response.isSuccessful) {
                    val updatedResponse = response.body()
                    // 수정된 메모 내용 확인 및 처리
                    Log.d("calendarviewmodify", "memo 수정 성공")
                    goToNewActivity() // 새로운 홈 화면으로 이동
                } else {
                    // API 호출이 실패한 경우 처리
                    val errorBody = response.errorBody() // 에러 응답 데이터 추출
                    Log.e("calendarviewmodify", "memo 수정 실패: ${errorBody?.string()}")
                }

            }
            override fun onFailure(call: Call<BaseResponse<MemoUpdateResponse>>, t: Throwable) {
                // API 호출 실패 처리
                Log.e("API Failure", "Error: ${t.message}", t)
            }
        })
    }
    private fun goToNewActivity() {
        val intent = Intent(this, CalendarDetailView1::class.java)
        startActivity(intent)
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
