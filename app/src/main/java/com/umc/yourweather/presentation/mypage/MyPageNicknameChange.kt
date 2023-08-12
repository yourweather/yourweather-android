package com.umc.yourweather.presentation.mypage

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.data.remote.request.ChangeNicknameRequest
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.UserResponse
import com.umc.yourweather.data.service.UserService
import com.umc.yourweather.databinding.ActivityMyPageNicknameChangeBinding
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.util.NicknameUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageNicknameChange : AppCompatActivity() {

    lateinit var binding: ActivityMyPageNicknameChangeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageNicknameChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nickname = intent.getStringExtra("nickname")

        // Log.d("닉네임", "$nickname")
        // btn 클릭할 때마다 랜덤 닉네임 추천
        binding.btnMypageNicknameRefresh.setOnClickListener {
            binding.etMypageNicknameNickname.setText("")
            binding.etMypageNicknameNickname.hint = NicknameUtils.getRandomHintText()
        }

        binding.flMypageNicknameBackbtn.setOnClickListener {
            finish()
        }

        binding.btnMypageNicknameNext.setOnClickListener {
            changeNickname()
        }
    }

    private fun changeNickname() {
        val newNickname = binding.etMypageNicknameNickname.text.toString()
        if (newNickname.isNotEmpty()) {
            performNicknameChange(newNickname)
        } else {
            // 닉네임이 비어있는 경우 처리
        }
    }

    private fun performNicknameChange(newNickname: String) {
        val userService = RetrofitImpl.authenticatedRetrofit.create(UserService::class.java)
        userService.changeNickname(ChangeNicknameRequest(newNickname))
            .enqueue(object : Callback<BaseResponse<UserResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<UserResponse>>,
                    response: Response<BaseResponse<UserResponse>>,
                ) {
                    if (response.isSuccessful) {
                        Log.d("닉네임 변경", "닉네임 변경 성공")
                        handleNicknameChangeResponse(response)
                    } else {
                        Log.d("닉네임 변경 실패", "API 호출 실패: ${response.code()}")
                        handleNicknameChangeResponse(response)
                    }
                }

                override fun onFailure(call: Call<BaseResponse<UserResponse>>, t: Throwable) {
                    Log.d("닉네임 변경 실패", "API 호출 실패: ${t.message}")
                    handleNicknameChangeFailure(t)
                }
            })
    }


    private fun handleNicknameChangeResponse(response: Response<BaseResponse<UserResponse>>) {
        if (response.isSuccessful) {
            val baseResponse = response.body()
            if (baseResponse != null && baseResponse.success) {
                val userResponse = baseResponse.result
                Log.d("닉네임 변경", "닉네임 변경 성공: ${userResponse?.nickname}")
                // 닉네임 변경 성공 처리 또는 다음 화면으로 이동 처리
            } else {
                Log.d("닉네임 변경", "닉네임 변경 실패")
                // 닉네임 변경 실패 처리
            }
        } else {
            Log.d("API 호출 실패", "API 호출 실패: ${response.code()}")
            // API 호출 실패 처리
        }
    }

    private fun handleNicknameChangeFailure(t: Throwable) {
        Log.d("API 호출 실패", "API 호출 실패: ${t.message}")
        // API 호출 실패 처리
    }
}
