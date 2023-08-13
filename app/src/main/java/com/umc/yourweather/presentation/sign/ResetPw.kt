package com.umc.yourweather.presentation.sign

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.R
import com.umc.yourweather.data.service.WeatherService
import com.umc.yourweather.databinding.ActivityPwResetBinding
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.util.SignUtils

class ResetPw : AppCompatActivity() {
    lateinit var binding: ActivityPwResetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPwResetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnResetPwNext.setOnClickListener {
            // 확인버튼
        }

        binding.btnResetPwLeftArrow.setOnClickListener {
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
    private fun resetPw(password : String){
    val service = RetrofitImpl.nonRetrofit.create(WeatherService::class.java)

//        Log.d("Calendar", "요청시작")
//        service.getMonthData(year = year, month = month)
    }
}
