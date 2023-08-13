package com.umc.yourweather.presentation.sign

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.VerifyEmailResponse
import com.umc.yourweather.data.service.UserService
import com.umc.yourweather.databinding.ActivityFindPwBinding
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.util.SignUtils
import com.umc.yourweather.util.SignUtils.Companion.ALERT_TEXT_FIND_PW
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FindPw : AppCompatActivity() {
    lateinit var binding: ActivityFindPwBinding
    private val retrofitWithoutToken = RetrofitImpl.nonRetrofit
    private lateinit var verifyEmailService: UserService

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
        verifyEmailService = retrofitWithoutToken.create(UserService::class.java)
        verifyEmailService.verifyEmail(email).enqueue(object : Callback<BaseResponse<VerifyEmailResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<VerifyEmailResponse>>,
                response: Response<BaseResponse<VerifyEmailResponse>>,
            ) {
                val responseBody = response.body()
                if (response.isSuccessful) {
                    val code = responseBody?.code
                    if (code == 200) {
                        // 성공한 경우
                        Log.d("VerifyEmailDebug", "이메일 전송 성공")
                        val mIntent = Intent(this@FindPw, FindPwEmail::class.java)
                        mIntent.putExtra("email", email)
                        startActivity(mIntent)
                    }
                } else if (responseBody?.result == null) {
                    SignUtils.customSingInPopupWindow(
                        this@FindPw,
                        ALERT_TEXT_FIND_PW,
                        binding.root,
                        binding.btnFindpwNext,
                    )
                }
            }

            override fun onFailure(call: Call<BaseResponse<VerifyEmailResponse>>, t: Throwable) {
                // 네트워크 에러 처리
                Log.d("VerifyEmailDebug", "네트워크 오류: " + t.message.toString())
            }
        })
    }
}
