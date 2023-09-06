package com.umc.yourweather.presentation.sign

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.R
import com.umc.yourweather.data.remote.request.ResetPasswordRequest
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.ResetPwResponse
import com.umc.yourweather.data.service.UserService
import com.umc.yourweather.databinding.ActivityPwResetBinding
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.util.SignUtils
import com.umc.yourweather.util.SignUtils.Companion.ALERT_TEXT_CHANGE_PW
import com.umc.yourweather.util.SignUtils.Companion.ALERT_TEXT_CHANGE_PW_ERROR
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPwActivity : AppCompatActivity() {
    lateinit var binding: ActivityPwResetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPwResetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnResetPwNext.setOnClickListener {
            // 확인버튼
            resetPwApi(binding.etResetPwRepw.text.toString())
        }

        binding.btnResetPwLeftArrow.setOnClickListener {
            finish()
        }

        binding.flResetPwBackbtn.setOnClickListener {
            finish()
        }

        binding.etResetPw.addTextChangedListener(createTextWatcher(::checkPwFormat))
        binding.etResetPwRepw.addTextChangedListener(createTextWatcher(::checkRePw))
    }

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

    private fun checkPwFormat() {
        var pw = binding.etResetPw.text.toString()

        if (SignUtils.isValidPassword(pw) == true) {
            binding.etResetPw.background = resources.getDrawable(R.drawable.bg_gray_ed_fill_6_rect)
            binding.tvResetPwCheck1.setTextColor(Color.parseColor("#99808080"))
        } else {
            binding.etResetPw.background = resources.getDrawable(R.drawable.bg_gray_ed_fill_6_rect_border_red)
            binding.tvResetPwCheck1.setTextColor(Color.parseColor("#CB443D"))
        }
    }

    private fun checkRePw() {
        var pw0 = binding.etResetPw.text.toString()
        var pw1 = binding.etResetPwRepw.text.toString()

        if (pw0.equals(pw1)) {
            binding.etResetPwRepw.background = resources.getDrawable(R.drawable.bg_gray_ed_fill_6_rect)

            binding.tvResetPwCheck2.visibility = View.INVISIBLE
            binding.ivSignup2Check0.visibility = View.VISIBLE
            binding.ivResetPwCheck1.visibility = View.VISIBLE

            binding.btnResetPwNext.isEnabled = true
        } else {
            binding.etResetPwRepw.background = resources.getDrawable(R.drawable.bg_gray_ed_fill_6_rect_border_red)

            binding.tvResetPwCheck2.visibility = View.VISIBLE
            binding.ivSignup2Check0.visibility = View.INVISIBLE
            binding.ivResetPwCheck1.visibility = View.INVISIBLE

            binding.btnResetPwNext.isEnabled = false
        }
    }

    private fun resetPwApi(password: String) {
        val service = RetrofitImpl.authenticatedRetrofit.create(UserService::class.java)

        service.changePw(ResetPasswordRequest(password)).enqueue(
            (
                object :
                    Callback<BaseResponse<ResetPwResponse>> {

                    override fun onResponse(
                        call: Call<BaseResponse<ResetPwResponse>>,
                        response: Response<BaseResponse<ResetPwResponse>>,
                    ) {
                        val responseBody = response.body()
                        val code = responseBody?.code

                        if (response.isSuccessful) {
                            if (code == 200) {
                                Log.d("ResetPw", "비번바꾸기 성공~ : " + response.headers().toString())
                                showCustomAlertDialog(ALERT_TEXT_CHANGE_PW, 0, true)
                            } else {
                                Log.d("ResetPw", "실패..")
                            }
                        } else {
                            showCustomAlertDialog(ALERT_TEXT_CHANGE_PW_ERROR, 0, true)
                            Log.d(
                                "ResetPw",
                                "onResponse 오류: ${response?.toString()}",
                            )
                        }
                    }
                    override fun onFailure(call: Call<BaseResponse<ResetPwResponse>>, t: Throwable) {
                        Log.d("ResetPw", "onFailure 에러: " + t.message.toString())
                    }
                }
                ),
        )
    }
    fun showCustomAlertDialog(text: String, flag: Int, isSuccess: Boolean) {
        val layoutInflater = LayoutInflater.from(this@ResetPwActivity)
        val customLayout = layoutInflater.inflate(R.layout.alertdialog_signview, null)

        val titleTextView = customLayout.findViewById<TextView>(R.id.tv_signview_alert)
        val alertButton = customLayout.findViewById<Button>(R.id.btn_signview_alert)

        val alertDialogBuilder = AlertDialog.Builder(this@ResetPwActivity)
        alertDialogBuilder.setView(customLayout)
        alertDialogBuilder.setCancelable(true)

        val alertDialog = alertDialogBuilder.create()
        alertDialog.setCancelable(false)

        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)

        alertDialog.show()

        titleTextView.text = text

        alertButton.setOnClickListener {
            alertDialog.dismiss()
            val mIntent = Intent(this@ResetPwActivity, SignInActivity::class.java)
            startActivity(mIntent)
            finish()
        }
    }
}
