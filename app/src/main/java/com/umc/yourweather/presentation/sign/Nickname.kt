package com.umc.yourweather.presentation.sign

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.data.remote.request.SignupRequest
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.service.UserService
import com.umc.yourweather.databinding.ActivityNicknameBinding
import com.umc.yourweather.di.App
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.util.NicknameUtils.Companion.getRandomHintText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Nickname : AppCompatActivity() {

    lateinit var binding: ActivityNicknameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNicknameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val editText = binding.etNicknameNickname
        val button = binding.btnNicknameRefresh

        // btn 클릭할 때마다 랜덤 닉네임 추천
        button.setOnClickListener {
            editText.setText("")
            editText.hint = getRandomHintText()
        }

        binding.btnNicknameNext.setOnClickListener {
            val enteredNickname = editText.text.toString() // 사용자가 입력한 닉네임

            // 사용자가 아무런 글자를 입력하지 않았다면, hint 값을 닉네임으로 사용
            val fixedNickname = if (enteredNickname.isBlank()) {
                editText.hint.toString()
            } else {
                enteredNickname
            }

            App.globalNickname = fixedNickname // 전역 닉네임 변수에 값을 설정

            // 회원 가입 API 호출
            User()
        }
    }

    private fun User() {
        val pw = intent.getStringExtra("password") ?: ""
        val email = intent.getStringExtra("email") ?: ""
        val platform = intent.getStringExtra("platform") ?: ""

        val enteredNickname = binding.etNicknameNickname.text.toString() // 사용자가 입력한 닉네임
        val hintNickname = binding.etNicknameNickname.hint.toString() // hint로 설정된 닉네임

        val fixedNickname = if (enteredNickname.isBlank()) {
            hintNickname
        } else {
            enteredNickname
        }

        val signupRequest = SignupRequest(email, pw, fixedNickname, platform)
        val signupService = RetrofitImpl.nonRetrofit.create(UserService::class.java)

        signupService.signUp(signupRequest).enqueue(object : Callback<BaseResponse<String>> {
            override fun onResponse(
                call: Call<BaseResponse<String>>,
                response: Response<BaseResponse<String>>,
            ) {
                if (response.isSuccessful) {
                    val code = response.body()?.code
                    if (code == 200) {
                        // 회원 가입 성공
                        Log.d("SignupDebug", "회원 가입 성공")
                    } else {
                        // 회원 가입 실패
                        Log.d("SignupDebug", "회원 가입 실패: code = $code")
                    }
                } else {
                    // API 호출 실패
                    Log.d("SignupDebug", "API 호출 실패")
                }
            }

            override fun onFailure(call: Call<BaseResponse<String>>, t: Throwable) {
                // 네트워크 에러 처리
                Log.d("SignupDebug", "네트워크 오류: " + t.message.toString())
            }
        })
    }
}
