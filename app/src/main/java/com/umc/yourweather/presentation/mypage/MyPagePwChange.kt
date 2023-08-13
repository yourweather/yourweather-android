package com.umc.yourweather.presentation.mypage

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.R
import com.umc.yourweather.data.remote.request.ChangeNicknameRequest
import com.umc.yourweather.data.remote.request.ChangePasswordRequest
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.UserResponse
import com.umc.yourweather.data.service.UserService
import com.umc.yourweather.databinding.ActivityMyPagePwChangeBinding
import com.umc.yourweather.di.MySharedPreferences
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.util.SignUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPagePwChange : AppCompatActivity() {
    lateinit var binding: ActivityMyPagePwChangeBinding
    var flag = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPagePwChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.flMypagePwBackbtn.setOnClickListener {
            finish()
        }

        binding.etMypagePwPw.addTextChangedListener(createTextWatcher(::checkPwFormat))
        binding.etMypagePwRepw.addTextChangedListener(createTextWatcher(::checkRePw))
        binding.etMypagePwMypw.addTextChangedListener(createTextWatcher(::checkMyPw))
    }

    private fun createTextWatcher(checkError: () -> Unit): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                checkError()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // 입력하기 전
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // 텍스트 변화가 있을 시
                checkError()
            }
        }
    }

    private fun checkMyPw() {
        val userPw = MySharedPreferences.getUserPw(this)

        var inputMyPW = binding.etMypagePwMypw.text.toString()
        if (inputMyPW.equals(userPw)) {
            Log.d("기존 비밀번호 : ", "$userPw")

            binding.etMypagePwMypw.background = resources.getDrawable(R.drawable.bg_gray_ed_fill_6_rect)
            binding.tvMypagePwMypwCheck.visibility = View.INVISIBLE
            binding.ivMypagePwPwCheck0.visibility = View.VISIBLE
            flag = 1
        } else {
            Log.d("기존 비밀번호 : ", "$userPw")

            binding.etMypagePwMypw.background = resources.getDrawable(R.drawable.bg_gray_ed_fill_6_rect_border_red)
            binding.tvMypagePwMypwCheck.visibility = View.VISIBLE
            binding.ivMypagePwPwCheck0.visibility = View.INVISIBLE
            flag = 0
        }
    }
    private fun checkPwFormat() {
        var newPw = binding.etMypagePwPw.text.toString()
        if (SignUtils.isValidPassword(newPw) == true) {
            binding.etMypagePwPw.background = resources.getDrawable(R.drawable.bg_gray_ed_fill_6_rect)
            binding.tvMypagePwCheck1.setTextColor(Color.parseColor("#99808080"))
        } else {
            binding.etMypagePwPw.background = resources.getDrawable(R.drawable.bg_gray_ed_fill_6_rect_border_red)
            binding.tvMypagePwCheck1.setTextColor(Color.parseColor("#CB443D"))
        }
    }
    private fun checkRePw() {
        var newPw = binding.etMypagePwPw.text.toString()
        var reNewPw = binding.etMypagePwRepw.text.toString()

        if (newPw.equals(reNewPw)) {
            binding.etMypagePwRepw.background = resources.getDrawable(R.drawable.bg_gray_ed_fill_6_rect)

            binding.tvMypagePwCheck2.visibility = View.INVISIBLE
            binding.ivMypagePwCheck1.visibility = View.VISIBLE
            binding.ivMypagePwCheck2.visibility = View.VISIBLE

            binding.btnMypagePwNext.isEnabled = flag == 1

            // API 호출
            changePwAPI(newPw)

        } else {
            binding.etMypagePwRepw.background = resources.getDrawable(R.drawable.bg_gray_ed_fill_6_rect_border_red)

            binding.tvMypagePwCheck2.visibility = View.VISIBLE
            binding.ivMypagePwCheck1.visibility = View.INVISIBLE
            binding.ivMypagePwCheck2.visibility = View.INVISIBLE

            binding.btnMypagePwNext.isEnabled = false
        }
    }

    private fun changePwAPI(newPw: String) {
        if (newPw.isNotEmpty()) {
            performNicknameChange(newPw)
        } else {
            // 닉네임이 비어있는 경우 처리
        }
    }

    private fun performNicknameChange(newPw: String) {
        val userService = RetrofitImpl.authenticatedRetrofit.create(UserService::class.java)
        userService.changePw(ChangePasswordRequest(newPw))
            .enqueue(object : Callback<BaseResponse<UserResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<UserResponse>>,
                    response: Response<BaseResponse<UserResponse>>,
                ) {
                    if (response.isSuccessful) {
                        Log.d("비밀번호 변경", "비밀번호 변경 성공")
                        handlePwChangeResponse(response)
                        binding.btnMypagePwNext.setOnClickListener {
                            finish()
                        }
                    } else {
                        Log.d("비밀번호 변경 실패", "API 호출 실패: ${response.code()}")
                        handlePwChangeResponse(response)
                    }
                }

                override fun onFailure(call: Call<BaseResponse<UserResponse>>, t: Throwable) {
                    Log.d("비밀번호 변경 실패", "API 호출 실패: ${t.message}")
                    handlePwChangeFailure(t)
                }
            })
    }


    private fun handlePwChangeResponse(response: Response<BaseResponse<UserResponse>>) {
        if (response.isSuccessful) {
            val baseResponse = response.body()
            if (baseResponse != null && baseResponse.success) {
                val userResponse = baseResponse.result
                Log.d("비밀번호 변경", "비밀번호 변경 성공: ${userResponse?.nickname}")
                // 비밀번호 변경 성공 처리 또는 다음 화면으로 이동 처리
            } else {
                Log.d("비밀번호 변경", "비밀번호 변경 실패")
                // 비밀번호 변경 실패 처리
            }
        } else {
            Log.d("비밀번호 변경 API 호출 실패", "API 호출 실패: ${response.code()}")
            // API 호출 실패 처리
        }
    }

    private fun handlePwChangeFailure(t: Throwable) {
        Log.d("API 호출 실패", "API 호출 실패: ${t.message}")
        // API 호출 실패 처리
    }
}
