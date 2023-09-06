package com.umc.yourweather.presentation.mypage

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.service.PolicyService
import com.umc.yourweather.databinding.ActivityMyPagePrivacyPolicyBinding
import com.umc.yourweather.di.RetrofitImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPagePrivacyPolicyActivity : AppCompatActivity() {
    lateinit var binding: ActivityMyPagePrivacyPolicyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPagePrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 버튼 클릭으로 되돌아가기
        binding.btnPrivacyPolicyBack.setOnClickListener {
            finish()
        }
        // 영역 클릭으로 되돌아가기
        binding.flPrivacyPolicyBack.setOnClickListener {
            finish()
        }
        val policyService = RetrofitImpl.authenticatedRetrofit.create(PolicyService::class.java)
        val call = policyService.policyPrivacy()

        call.enqueue(object : Callback<BaseResponse<String>> {
            override fun onResponse(
                call: Call<BaseResponse<String>>,
                response: Response<BaseResponse<String>>,
            ) {
                if (response.isSuccessful) {
                    val baseResponse = response.body()
                    if (baseResponse != null) {
                        val content = baseResponse.result
                        binding.tvPrivacyPolicyContent.text = content
                    } else {
                        Log.e("MyPagePrivacyPolicy", "응답 content가 null입니다.")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("MyPagePrivacyPolicy", "응답 실패")
                }
            }

            override fun onFailure(call: Call<BaseResponse<String>>, t: Throwable) {
                Log.e("MyPagePrivacyPolicy", "API호출 실패")
            }
        })
    }
}