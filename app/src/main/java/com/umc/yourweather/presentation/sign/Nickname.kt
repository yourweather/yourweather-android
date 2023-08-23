package com.umc.yourweather.presentation.sign

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.R
import com.umc.yourweather.data.remote.request.SignupRequest
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.TokenResponse
import com.umc.yourweather.data.service.UserService
import com.umc.yourweather.databinding.ActivityNicknameBinding
import com.umc.yourweather.di.App
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.di.UserSharedPreferences
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

        val editText = binding.etNicknameNickname
        val button = binding.btnNicknameRefresh

        // 버튼을 클릭할 때마다 랜덤 닉네임 추천
        button.setOnClickListener {
            editText.setText("")
            editText.hint = getRandomHintText()
        }

        // 8글자 이상 입력을 제한
        editText.addTextChangedListener(object : TextWatcher {

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

        binding.btnNicknameNext.setOnClickListener {
            val enteredNickname = editText.text.toString() // 사용자가 입력한 닉네임

            // 사용자가 아무런 글자를 입력하지 않았다면, 힌트 값을 닉네임으로 사용
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

        signupService.signUp(signupRequest).enqueue(object : Callback<BaseResponse<TokenResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<TokenResponse>>,
                response: Response<BaseResponse<TokenResponse>>,
            ) {
                if (response.isSuccessful) {
                    val code = response.body()?.code
                    if (code == 200) {
                        // 회원 가입 성공
                        Log.d("SignupDebug", "회원 가입 성공")

                        // 자동로그인
                        UserSharedPreferences.setUserEmail(this@Nickname, email)
                        UserSharedPreferences.setUserPw(this@Nickname, pw)
                        //

                        // 토큰저장
                        App.token_prefs.accessToken = response.body()!!.result?.accessToken
                        App.token_prefs.refreshToken = response.body()!!.result?.refreshToken
                        // 토큰저장

                        UserSharedPreferences.setUserPwToStar(this@Nickname, pw)
                        UserSharedPreferences.setUserPlatform(this@Nickname, platform)
                        UserSharedPreferences.setUserNickname(this@Nickname, fixedNickname)

                        Log.d("회원가입 sh 확인 로그 이메일", UserSharedPreferences.getUserEmail(this@Nickname))
                        Log.d("회원가입 sh 확인 로그 비번", UserSharedPreferences.getUserPw(this@Nickname))
                        Log.d("회원가입 sh 확인 로그 서버에서 온 액세스 토큰", App.token_prefs.accessToken.toString())
                        Log.d("회원가입 sh 확인 로그 서버에서 온 리프래시 토큰", App.token_prefs.refreshToken.toString())
                        Log.d("회원가입 sh 확인 로그 저장한 액세스 토큰", App.token_prefs.accessToken.toString())
                        Log.d("회원가입 sh 확인 로그 저장한 리프래시 토큰", App.token_prefs.refreshToken.toString())

                        // 회원 가입 성공 후 홈화면으로 이동
                        val mIntent = Intent(this@Nickname, BottomNavi::class.java)
                        mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        showInitialToast()
                        startActivity(mIntent)
                    } else {
                        // 회원 가입 실패
                        Log.d("SignupDebug", "회원 가입 실패: code = $code")
                    }
                } else {
                    // API 호출 실패
                    Log.d("SignupDebug", "API 호출 실패")
                }
            }

            override fun onFailure(call: Call<BaseResponse<TokenResponse>>, t: Throwable) {
                // 네트워크 에러 처리
                Log.d("SignupDebug", "네트워크 오류: " + t.message.toString())
            }
        })
    }

    private fun showInitialToast() {
        val toastView = layoutInflater.inflate(R.layout.toast_initial, binding.root, false)
        val toast = Toast.makeText(this, "", Toast.LENGTH_SHORT)
        toast.view = toastView
        toast.setGravity(Gravity.BOTTOM or Gravity.CENTER, 0, resources.getDimensionPixelSize(R.dimen.initial_toast_margin_bottom))
        toast.show()
    }
}
