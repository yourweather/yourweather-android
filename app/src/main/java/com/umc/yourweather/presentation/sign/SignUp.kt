package com.umc.yourweather.presentation.sign

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
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
import com.umc.yourweather.util.SignUtils.Companion.createTextWatcher
import com.umc.yourweather.util.SignUtils.Companion.setAlertText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUp : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    private var countDownTimer: CountDownTimer? = null

    // Retrofit을 이용한 이메일 전송 서비스 생성
    private val retrofitWithoutToken = RetrofitImpl.nonRetrofit
    private val emailService = retrofitWithoutToken.create(EmailService::class.java)

    // 이메일 인증 여부를 나타내는 변수
    private var isEmailCertified = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 아래 동의 문구 글자 색깔 지정해주는 코드
        setAlertText(this@SignUp, binding.root, R.id.tv_signup_alertText)

        // "뒤로 가기" 버튼 클릭 시 이벤트 처리
        binding.btnSingupBack.setOnClickListener {
            val mIntent = Intent(this, SignIn::class.java)
            startActivity(mIntent)
            finish()
        }

        binding.flSignupBackbtn.setOnClickListener {
            finish()
        }

        // "로그인하러 가기" 버튼 클릭 시 이벤트 처리
        binding.tvSignupBtnsignin.setOnClickListener {
            val mIntent = Intent(this, SignIn::class.java)
            startActivity(mIntent)
            finish()
        }

        // "다음" 버튼 클릭 시 이벤트 처리
        binding.btnSignupNext.setOnClickListener {
            userEmail()
        }

        // 이메일 입력란 텍스트 변화 감지
        binding.etSignupEmail.addTextChangedListener(createTextWatcher(::checkEmailError))
        binding.etSignupAuth.addTextChangedListener(createTextWatcher(::checkAuth))

        // "인증코드 전송" 버튼 클릭 시 이벤트 처리
        binding.btnSignupSendauth.setOnClickListener {
            val email = binding.etSignupEmail.text.toString()
            sendEmail(email)
        }

        // "인증 확인" 버튼 클릭 시 이벤트 처리
        binding.btnSignupCheckauth.setOnClickListener {
            val email = binding.etSignupEmail.text.toString()
            val authCode = binding.etSignupAuth.text.toString()
            certifyEmail(email, authCode)
        }

        // "인증코드 재전송" 버튼 클릭 시 이벤트 처리
        binding.tvSignupBtnresend.setOnClickListener {
            countDownTimer?.cancel() // 타이머 취소
            val email = binding.etSignupEmail.text.toString()
            resendEmail(email)
        }
    }

    // TextWatcher를 생성하는 함수

    // 이메일 유효성 검사
    private fun checkEmailError() {
        val email = binding.etSignupEmail.text.toString()
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etSignupEmail.background =
                resources.getDrawable(R.drawable.bg_gray_ed_fill_6_rect)
            binding.tvSignupError.visibility = View.INVISIBLE
            binding.btnSignupSendauth.isEnabled = true
        } else {
            binding.etSignupEmail.background =
                resources.getDrawable(R.drawable.bg_gray_ed_fill_6_rect_border_red)
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

    // 0 : 인증코드 전송 관련
    // 1 : 인증 성공 여부
    // 커스텀 다이얼로그 표시
    fun showCustomAlertDialog(text: String, flag: Int, isSuccess: Boolean) {
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
                    if (isSuccess) {
                        countDownTimer?.cancel()
                        // 타이머를 시작하는 동작
                        startTimer()
                    } else {
                        // 타이머 시작 실패에 대한 처리
                    }
                }

                1 -> {
                    if (isSuccess) {
                        // 인증 성공에 대한 처리
                        binding.btnSignupNext.isEnabled = true
                        countDownTimer?.cancel()
                    } else {
                        // 인증 실패에 대한 처리
                    }
                }
            }
        }

        alertDialog.setOnDismissListener {
            // 다이얼로그가 사라질 때의 처리
        }
    }

    // 이메일 전송 API 호출
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
                        showCustomAlertDialog("인증코드가 전송되었습니다.", 0, true)
                    } else {
                        // 실패한 경우
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

    // 이메일 인증 API 호출
    private fun certifyEmail(email: String, code: String) {
        emailService.certifyEmail(email, code).enqueue(object : Callback<BaseResponse<Boolean>> {
            override fun onResponse(
                call: Call<BaseResponse<Boolean>>,
                response: Response<BaseResponse<Boolean>>,
            ) {
                if (response.isSuccessful) {
                    val code = response.body()?.code
                    if (code == 200) {
                        // 인증 성공한 경우
                        Log.d("CertifyEmailDebug", "이메일 인증 성공")
                        showCustomAlertDialog("인증 성공했습니다.", 1, true)
                        // 이메일 인증이 완료되었으므로 버튼 활성화 및 상태 변경
                        isEmailCertified = true
                        binding.btnSignupNext.isEnabled = true
                        countDownTimer?.cancel()
                    } else {
                        // 인증 실패한 경우
                        Log.d("CertifyEmailDebug", "이메일 인증 실패")
                        showCustomAlertDialog("인증 실패했습니다.", 1, false)
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<Boolean>>, t: Throwable) {
                // 네트워크 에러 처리
                Log.d("CertifyEmailDebug", "네트워크 오류: " + t.message.toString())
            }
        })
    }

    // 이메일 재전송 API 호출
    private fun resendEmail(email: String) {
        emailService.sendEmail(email).enqueue(object : Callback<BaseResponse<Unit>> {
            override fun onResponse(
                call: Call<BaseResponse<Unit>>,
                response: Response<BaseResponse<Unit>>,
            ) {
                if (response.isSuccessful) {
                    val code = response.body()?.code
                    if (code == 200) {
                        // 성공한 경우
                        Log.d("ResendEmailDebug", "이메일 재전송 성공")
                        showCustomAlertDialog("인증코드가 재전송되었습니다.", 0, true)
                    } else {
                        // 실패한 경우
                        Log.d("ResendEmailDebug", "이메일 재전송 실패: code = $code")
                        showCustomAlertDialog("인증코드 재전송 실패했습니다.", 0, false)
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<Unit>>, t: Throwable) {
                // 네트워크 에러 처리
                Log.d("ResendEmailDebug", "네트워크 오류: " + t.message.toString())
            }
        })
    }
}
