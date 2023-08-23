package com.umc.yourweather.presentation.mypage

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.data.remote.request.ChangeNicknameRequest
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.UserResponse
import com.umc.yourweather.data.service.UserService
import com.umc.yourweather.databinding.ActivityMyPageNicknameChangeBinding
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.di.UserSharedPreferences
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

        binding.btnMypageNicknameRefresh.setOnClickListener {
            binding.etMypageNicknameNickname.setText("")
            binding.etMypageNicknameNickname.hint = NicknameUtils.getRandomHintText()
        }

        binding.flMypageNicknameBackbtn.setOnClickListener {
            finish()
        }

        binding.btnMypageNicknameNext.setOnClickListener {
            changeNickname()
            finish()
        }
        // 8글자 이상 입력을 제한하는 부분 추가
        binding.etMypageNicknameNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length ?: 0 > 8) {
                    s?.delete(8, s.length)
                }
            }
        })
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
                    handleNicknameChangeResponse(response, newNickname)
                }

                override fun onFailure(call: Call<BaseResponse<UserResponse>>, t: Throwable) {
                    handleNicknameChangeFailure(t)
                }
            })
    }

    private fun handleNicknameChangeResponse(
        response: Response<BaseResponse<UserResponse>>,
        newNickname: String,
    ) {
        if (response.isSuccessful) {
            val baseResponse = response.body()
            if (baseResponse != null && baseResponse.success) {
                // 기기에 변경된 닉네임 저장
                UserSharedPreferences.setUserNickname(this, newNickname)

                // SharedPreferences 갱신
                val prefs = getSharedPreferences("account", Context.MODE_PRIVATE)
                prefs.edit().putString(UserSharedPreferences.USER_NICKNAME, newNickname).apply()

                Toast.makeText(
                    this@MyPageNicknameChange,
                    "닉네임이 변경되었습니다.",
                    Toast.LENGTH_SHORT,
                ).show()
                Log.d("닉네임 변경", "닉네임 변경 성공: $newNickname")
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
