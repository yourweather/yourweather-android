package com.umc.yourweather.presentation.sign

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.umc.yourweather.R
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.service.EmailService
import com.umc.yourweather.databinding.ActivityFindPwEmailBinding
import com.umc.yourweather.di.RetrofitImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FindPwEmail : AppCompatActivity() {
//    lateinit var binding: ActivityFindPwEmailBinding
//    private val retrofitWithoutToken = RetrofitImpl.nonRetrofit
//    private val emailService = retrofitWithoutToken.create(EmailService::class.java)
//    // 이메일 인증 여부를 나타내는 변수
//    private var isEmailCertified = false
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityFindPwEmailBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        binding.btnFindPwEmailNext.setOnClickListener {
//            val mIntent = Intent(this, ResetPw::class.java)
//            startActivity(mIntent)
//        }
//        binding.btnFindPwEmailLeftArrow.setOnClickListener {
//            finish()
//        }
//    }
//
//
//
//    // 이메일 인증 API 호출
//    private fun certifyEmail(email: String, code: String) {
//        emailService.certifyEmail(email, code).enqueue(object : Callback<BaseResponse<Boolean>> {
//            override fun onResponse(
//                call: Call<BaseResponse<Boolean>>,
//                response: Response<BaseResponse<Boolean>>,
//            ) {
//                if (response.isSuccessful) {
//                    val code = response.body()?.code
//                    if (code == 200) {
//                        // 인증 성공한 경우
//                        Log.d("CertifyEmailDebug", "이메일 인증 성공")
//                        showCustomAlertDialog("인증 성공했습니다.", 1, true)
//                        // 이메일 인증이 완료되었으므로 버튼 활성화 및 상태 변경
//                        isEmailCertified = true
//                        binding.btnSignupNext.isEnabled = true
//                        countDownTimer?.cancel()
//                    } else {
//                        // 인증 실패한 경우
//                        Log.d("CertifyEmailDebug", "이메일 인증 실패")
//                        showCustomAlertDialog("인증 실패했습니다.", 1, false)
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<BaseResponse<Boolean>>, t: Throwable) {
//                // 네트워크 에러 처리
//                Log.d("CertifyEmailDebug", "네트워크 오류: " + t.message.toString())
//            }
//        })
//    }
}