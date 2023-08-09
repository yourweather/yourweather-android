package com.umc.yourweather.presentation.sign

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.R
import com.umc.yourweather.data.remote.request.SignupRequest
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.service.UserService
import com.umc.yourweather.databinding.ActivityNicknameBinding
import com.umc.yourweather.di.App
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.presentation.BottomNavi
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

        val editText: EditText = findViewById(R.id.et_nickname_nickname)
        val button: Button = findViewById(R.id.btn_nickname_refresh)

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
            val mIntent = Intent(this, BottomNavi::class.java)
            startActivity(mIntent)
        }
        User()
    }

    private fun User() {
        val pw = intent.getStringExtra("password") ?: ""
        val email = intent.getStringExtra("email") ?: ""
        Log.d("pwDebug2", "pw value2: $pw")
        Log.d("EmailDebug3", "Email value3: $email")
        val platform = intent.getStringExtra("platform") ?: ""
        val fixedNickname = App.globalNickname ?: ""

        // 회원 가입 API 호출
        val signupRequest = SignupRequest(email, pw, fixedNickname, platform)
        val signupService = RetrofitImpl.nonRetrofit.create(UserService::class.java)
        signupService.signUp(signupRequest).enqueue(object : Callback<BaseResponse<String>> {
            override fun onResponse(
                call: Call<BaseResponse<String>>,
                response: Response<BaseResponse<String>>
            ) {
                if (response.isSuccessful) {
                    val code = response.body()?.code
                    if (code == 200) {
                        // 회원 가입 성공
                        Log.d("SignupDebug", "회원 가입 성공")
                        
                    } else {
                        // 회원 가입 실패
                        Log.d("SignupDebug", "회원 가입 실패: code = $code")
                        // 원하는 실패 처리 작업 수행
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<String>>, t: Throwable) {
                // 네트워크 에러 처리
                Log.d("SignupDebug", "네트워크 오류: " + t.message.toString())
                // 원하는 실패 처리 작업 수행
            }
        })
    }
}

