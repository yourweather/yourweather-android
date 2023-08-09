package com.umc.yourweather.presentation.sign

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.R
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.service.EmailService
import com.umc.yourweather.databinding.ActivitySignUpBinding
import com.umc.yourweather.di.RetrofitImpl
import retrofit2.Call
import retrofit2.Response


class SignUp : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    private lateinit var countDownTimer: CountDownTimer

    // Retrofit을 이용한 이메일 전송 서비스 생성
    private val retrofitWithoutToken = RetrofitImpl.nonRetrofit
    private val emailService = retrofitWithoutToken.create(EmailService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSingupBack.setOnClickListener {
            val mIntent = Intent(this, SignIn::class.java)
            startActivity(mIntent)
            finish()
        }

        binding.tvSignupBtnsignin.setOnClickListener {
            val mIntent = Intent(this, SignIn::class.java)
            startActivity(mIntent)
            finish()
        }

        binding.btnSignupNext.setOnClickListener {
            userEmail()
        }

        binding.etSignupEmail.addTextChangedListener(createTextWatcher(::checkEmailError))
        binding.etSignupAuth.addTextChangedListener(createTextWatcher(::checkAuth))

        binding.btnSignupSendauth.setOnClickListener {
            val email = binding.etSignupEmail.text.toString()

            // 이메일 전송 요청
            sendEmail(email)
        }

        binding.btnSignupCheckauth.setOnClickListener {
            showCustomAlertDialog("인증이 완료되었습니다.", 1)
        }

        binding.tvSignupBtnresend.setOnClickListener {
            countDownTimer?.cancel()
            showCustomAlertDialog("인증코드가 전송되었습니다.", 0)
        }
    }

    // TextWatcher를 생성하는 함수
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

    // 이메일 유효성 검사
    private fun checkEmailError() {
        val email = binding.etSignupEmail.text.toString()
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etSignupEmail.background = resources.getDrawable(R.drawable.bg_gray_ed_fill_6_rect)
            binding.tvSignupError.visibility = View.INVISIBLE
            binding.btnSignupSendauth.isEnabled = true
        } else {
            binding.etSignupEmail.background = resources.getDrawable(R.drawable.bg_gray_ed_fill_6_rect_border_red)
            binding.tvSignupError.visibility = View.VISIBLE
            binding.btnSignupSendauth.isEnabled = false
        }
    }

    // 다음 단계로 이동하는 함수
    private fun userEmail() {
        val email = binding.etSignupEmail.text.toString()
        val mIntent = Intent(this@SignUp, SignUp2::class.java)
        mIntent.putExtra("email", email)
        startActivity(mIntent)
        Log.d("EmailDebug", "Email value: $email")
    }

    // 인증 코드 길이 확인
    private fun checkAuth() {
        val authCode = binding.etSignupAuth.text.toString()
        binding.btnSignupCheckauth.isEnabled = (authCode.length == 6)
    }

    // 타이머 시작
    private fun startTimer() {
        val startTimeMillis = 5 * 60 * 1000 // 3 minutes in milliseconds
        countDownTimer = object : CountDownTimer(startTimeMillis.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / (60 * 1000)
                val seconds = (millisUntilFinished % (60 * 1000)) / 1000
                binding.tvSignupTime.visibility = View.VISIBLE
                binding.tvSignupTime.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                binding.tvSignupTime.text = "00:00" // Timer finished
            }
        }.start()
    }

    // 커스텀 다이얼로그 표시
    fun showCustomAlertDialog(text: String, flag: Int) {
        val layoutInflater = LayoutInflater.from(this@SignUp)
        val customLayout = layoutInflater.inflate(R.layout.alertdialog_signview, null)

        val titleTextView = customLayout.findViewById<TextView>(R.id.tv_signview_alert)
        val alertButton = customLayout.findViewById<Button>(R.id.btn_signview_alert)

        val alertDialogBuilder = AlertDialog.Builder(this@SignUp)
        alertDialogBuilder.setView(customLayout)
        alertDialogBuilder.setCancelable(true)

        val alertDialog = alertDialogBuilder.create()

        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)

        alertDialog.show()

        titleTextView.text = text

        alertButton.setOnClickListener {
            alertDialog.dismiss()
            when (flag) {
                0 -> {
                    startTimer()
                }
                1 -> {
                    binding.btnSignupNext.isEnabled = true
                    countDownTimer?.cancel()
                }
            }
        }

        alertDialog.setOnDismissListener {
            // 다이얼로그가 사라질 때의 처리
        }
    }

    // 이메일 전송 요청 함수
    private fun sendEmail(email: String) {
        emailService.sendEmail(email).enqueue(object : retrofit2.Callback<BaseResponse<Unit>> {
            override fun onResponse(
                call: Call<BaseResponse<Unit>>,
                response: Response<BaseResponse<Unit>>,
            ) {
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        // 이메일 전송 성공 처리
                        showCustomAlertDialog("인증코드가 전송되었습니다.", 0)
                        binding.etSignupEmail.isEnabled = false
                    } else {
                        // 이메일 전송 실패 처리
                        Log.d("post", "이메일 전송 실패: " + response.body().toString())
                        showCustomAlertDialog("이메일 전송에 실패했습니다.", -1)
                    }
                } else {
                    // 이메일 전송 실패 처리
                    Log.d("post", "이메일 전송 실패: " + response.body().toString())
                    showCustomAlertDialog("이메일 전송에 실패했습니다.", -1)
                }
            }

            override fun onFailure(call: Call<BaseResponse<Unit>>, t: Throwable) {
                // 네트워크 에러 등 실패 처리
                Log.e("post", "이메일 전송 실패: " + t.message)
                showCustomAlertDialog("네트워크 에러입니다.", -1)
            }
        })
    }
}
