package com.umc.yourweather.presentation.mypage

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.R
import com.umc.yourweather.data.remote.request.ChangePasswordRequest
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.ChangePasswordRespond
import com.umc.yourweather.data.service.UserService
import com.umc.yourweather.databinding.ActivityMyPagePwChangeBinding
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.util.SignUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPagePwChangeActivity : AppCompatActivity() {
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
        // binding.etMypagePwMypw.addTextChangedListener(createTextWatcher(::checkMyPw))
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

//    private fun checkMyPw() {
//        val userPw = UserSharedPreferences.getUserPwToStar(this)
//
//        var inputMyPW = binding.etMypagePwMypw.text.toString()
//        if (inputMyPW.equals(userPw)) {
//            Log.d("기존 비밀번호 : ", "$userPw")
//
//            binding.etMypagePwMypw.background = resources.getDrawable(R.drawable.bg_gray_ed_fill_6_rect)
//            binding.tvMypagePwMypwCheck.visibility = View.INVISIBLE
//            binding.ivMypagePwPwCheck0.visibility = View.VISIBLE
//            flag = 1
//        } else {
//            Log.d("기존 비밀번호 : ", "$userPw")
//
//            binding.etMypagePwMypw.background = resources.getDrawable(R.drawable.bg_gray_ed_fill_6_rect_border_red)
//            binding.tvMypagePwMypwCheck.visibility = View.VISIBLE
//            binding.ivMypagePwPwCheck0.visibility = View.INVISIBLE
//            flag = 0
//        }
//    }
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
        val Pw = binding.etMypagePwMypw.text.toString()
        var newPw = binding.etMypagePwPw.text.toString()
        var reNewPw = binding.etMypagePwRepw.text.toString()

        if (newPw.equals(reNewPw)) {
            binding.etMypagePwRepw.background = resources.getDrawable(R.drawable.bg_gray_ed_fill_6_rect)

            binding.tvMypagePwCheck2.visibility = View.INVISIBLE
            binding.ivMypagePwCheck1.visibility = View.VISIBLE
            binding.ivMypagePwCheck2.visibility = View.VISIBLE

            binding.btnMypagePwNext.isEnabled = flag == 1
            binding.btnMypagePwNext.isEnabled = flag == 0

            // API 호출
            changePwAPI(Pw, newPw)
        } else {
            binding.etMypagePwRepw.background = resources.getDrawable(R.drawable.bg_gray_ed_fill_6_rect_border_red)

            binding.tvMypagePwCheck2.visibility = View.VISIBLE
            binding.ivMypagePwCheck1.visibility = View.INVISIBLE
            binding.ivMypagePwCheck2.visibility = View.INVISIBLE

            binding.btnMypagePwNext.isEnabled = false
        }
    }

    private fun changePwAPI(Pw: String, newPw: String) {
        if (newPw.isNotEmpty()) {
            binding.btnMypagePwNext.setOnClickListener {
                performPwChange(Pw, newPw)
            }
        } else {
            // 닉네임이 비어있는 경우 처리
        }
    }

    private fun performPwChange(Pw: String, newPw: String) {
        val userService = RetrofitImpl.authenticatedRetrofit.create(UserService::class.java)
        userService.changePw(ChangePasswordRequest(Pw, newPw))
            .enqueue(object : Callback<BaseResponse<ChangePasswordRespond>> {
                override fun onResponse(
                    call: Call<BaseResponse<ChangePasswordRespond>>,
                    response: Response<BaseResponse<ChangePasswordRespond>>,
                ) {
                    if (response.isSuccessful) {
                        val changePasswordRespond = response.body()?.result // Assuming 'data' is the property that holds the response body
                        if (changePasswordRespond != null && changePasswordRespond.success) {
                            Log.d("비밀번호 변경", "비밀번호 변경 성공")
                            handlePwChangeResponse(response)
                            Toast.makeText(
                                this@MyPagePwChangeActivity,
                                "비밀번호가 변경되었습니다.",
                                Toast.LENGTH_SHORT,
                            ).show()
                            finish()
                        } else {
                            Log.d("비밀번호 변경 실패", "API 호출 실패: ${response.code()}")
                            Toast.makeText(
                                this@MyPagePwChangeActivity,
                                "비밀번호가 변경되지 않았습니다. 다시 시도해주세요.",
                                Toast.LENGTH_SHORT,
                            ).show()
                            handlePwChangeResponse(response)
                        }
                    } else {
                        Log.d("비밀번호 변경 실패", "API 호출 실패: ${response.code()}")
                        handlePwChangeResponse(response)
                    }
                }

                override fun onFailure(call: Call<BaseResponse<ChangePasswordRespond>>, t: Throwable) {
                    Log.d("비밀번호 변경 실패", "API 호출 실패: ${t.message}")
                    handlePwChangeFailure(t)
                }
            })
    }

    private fun handlePwChangeResponse(response: Response<BaseResponse<ChangePasswordRespond>>) {
        if (response.isSuccessful) {
            val baseResponse = response.body()
            if (baseResponse != null && baseResponse.success) {
                val ChangePasswordRespond = baseResponse.result
                Log.d("비밀번호 변경", "비밀번호 변경 성공: ${ChangePasswordRespond?.success}")
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
