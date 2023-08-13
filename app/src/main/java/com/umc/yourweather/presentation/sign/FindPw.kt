package com.umc.yourweather.presentation.sign

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.service.EmailService
import com.umc.yourweather.databinding.ActivityFindPwBinding
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.util.SignUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FindPw : AppCompatActivity() {
    lateinit var binding: ActivityFindPwBinding
    private val retrofitWithoutToken = RetrofitImpl.nonRetrofit
    private val emailService = retrofitWithoutToken.create(EmailService::class.java)
    // 이메일 인증 여부를 나타내는 변수
    private var isEmailCertified = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindPwBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnFindpwNext.setOnClickListener {
            sendEmail(binding.etFindpwEmail.text.toString())
        }
        binding.btnFindpwBack.setOnClickListener {

            finish()
        }
    }

    private fun sendEmail(email: String) {
        emailService.sendEmail(email).enqueue(object : Callback<BaseResponse<Unit>> {
            override fun onResponse(
                call: Call<BaseResponse<Unit>>,
                response: Response<BaseResponse<Unit>>,
            ) {
                if (response.isSuccessful) {
                    val code = response.body()?.code
                    if (code == 200) {
                        // 성공한 경우
                        Log.d("SendEmailDebug", "이메일 전송 성공")
                        //val mIntent = Intent(this, FindPwEmail::class.java)
                        //startActivity(mIntent)

                    } else {
                        // 실패한 경우
//                        SignUtils.customSingInPopupWindow(
//                            this@SignIn,
//                            SignUtils.alertTextSignIn,
//                            binding.root,
//                            binding.btnSigninSignin
//                        )
                        Log.d("SendEmailDebug", "이메일 전송 실패: code = $code")
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<Unit>>, t: Throwable) {
                // 네트워크 에러 처리
                Log.d("SendEmailDebug", "네트워크 오류: " + t.message.toString())
            }
        })
    }
}
