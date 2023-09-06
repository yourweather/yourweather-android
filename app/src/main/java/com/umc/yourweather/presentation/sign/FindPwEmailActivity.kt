package com.umc.yourweather.presentation.sign

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
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
import com.umc.yourweather.databinding.ActivityFindPwEmailBinding
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.util.SignUtils
import com.umc.yourweather.util.SignUtils.Companion.ALERT_TEXT_FIND_PW_EMAIL
import com.umc.yourweather.util.SignUtils.Companion.createTextWatcher
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FindPwEmailActivity : AppCompatActivity() {
    lateinit var binding: ActivityFindPwEmailBinding
    private val retrofitWithoutToken = RetrofitImpl.nonRetrofit
    private val emailService = retrofitWithoutToken.create(EmailService::class.java)
    private var countDownTimer: CountDownTimer? = null

    // 이메일 인증 여부를 나타내는 변수
    private var isEmailCertified = false
    lateinit var email: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindPwEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        email = intent.getStringExtra("email")!!

        binding.tvFindPwUserEmail.text = email

        startTimer()
        binding.btnFindPwEmailNext.setOnClickListener {
            val mIntent = Intent(this, ResetPwActivity::class.java)
            startActivity(mIntent)
            finish()
        }

        binding.btnFindPwEmailCheckauth.setOnClickListener {
            certifyEmail(email, binding.etFindPwEmailAuth.text.toString())
        }

        binding.tvFindPwEmailBtnresend.setOnClickListener {
            countDownTimer?.cancel()
            resendEmail(email)
        }

        binding.btnFindPwEmailLeftArrow.setOnClickListener {
            finish()
        }

        binding.flFinpwEmailBackbtn.setOnClickListener {
            finish()
        }

        binding.etFindPwEmailAuth.addTextChangedListener(createTextWatcher(::checkAuth))
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
                        showCustomAlertDialog("인증이 완료되었습니다.", 1, true)
                        // 이메일 인증이 완료되었으므로 버튼 활성화 및 상태 변경
                        isEmailCertified = true
                        binding.btnFindPwEmailNext.isEnabled = true
                        countDownTimer?.cancel()
                    } else {
                        // 인증 실패한 경우
                        Log.d("CertifyEmailDebug", "이메일 인증 실패")
                        SignUtils.customSingInPopupWindow(
                            this@FindPwEmailActivity,
                            ALERT_TEXT_FIND_PW_EMAIL,
                            binding.root,
                            binding.btnFindPwEmailNext,
                        )
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<Boolean>>, t: Throwable) {
                // 네트워크 에러 처리
                Log.d("CertifyEmailDebug", "네트워크 오류: " + t.message.toString())
                val mIntent = Intent(this@FindPwEmailActivity, SignIn::class.java)
                startActivity(mIntent)
                finish()
            }
        })
    }

    // 재전송
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
                        showCustomAlertDialog("인증코드가 재전송되었습니다.", 0, true)
                        Log.d("ResendEmailDebug", "이메일 재전송 성공")
                    } else {
                        // 실패한 경우
                        Log.d("ResendEmailDebug", "이메일 재전송 실패: code = $code")
                        showCustomAlertDialog("인증코드 전송에 실패했습니다.", 0, false)
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<Unit>>, t: Throwable) {
                // 네트워크 에러 처리
                Log.d("ResendEmailDebug", "네트워크 오류: " + t.message.toString())
                val mIntent = Intent(this@FindPwEmailActivity, SignIn::class.java)
                startActivity(mIntent)
                finish()
            }
        })
    }

    private fun startTimer() {
        val startTimeMillis = 5 * 60 * 1000 // 3 minutes in milliseconds
        countDownTimer = object : CountDownTimer(startTimeMillis.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / (60 * 1000)
                val seconds = (millisUntilFinished % (60 * 1000)) / 1000
                binding.tvFindPwEmailTime.visibility = View.VISIBLE
                binding.tvFindPwEmailTime.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                binding.tvFindPwEmailTime.text = "00:00" // Timer finished
            }
        }.start()
    }

    private fun checkAuth() {
        val authCode = binding.etFindPwEmailAuth.text.toString()
        binding.btnFindPwEmailCheckauth.isEnabled = (authCode.length == 6)
    }

    // 0 : 인증코드 전송 관련
    // 1 : 인증 성공 여부
    fun showCustomAlertDialog(text: String, flag: Int, isSuccess: Boolean) {
        val layoutInflater = LayoutInflater.from(this@FindPwEmailActivity)
        val customLayout = layoutInflater.inflate(R.layout.alertdialog_signview, null)

        val titleTextView = customLayout.findViewById<TextView>(R.id.tv_signview_alert)
        val alertButton = customLayout.findViewById<Button>(R.id.btn_signview_alert)

        val alertDialogBuilder = AlertDialog.Builder(this@FindPwEmailActivity)
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
                        binding.btnFindPwEmailNext.isEnabled = true
                        countDownTimer?.cancel()
                    } else {
                        // 인증 실패에 대한 처리
                    }
                }
            }
        }
    }
}
