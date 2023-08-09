package com.umc.yourweather.presentation.mypage

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivityMyPagePwChangeBinding
import com.umc.yourweather.util.SignUtils

class MyPagePwChange : AppCompatActivity() {
    lateinit var binding: ActivityMyPagePwChangeBinding
    var flag = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPagePwChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMypagePwNext.setOnClickListener {
//            val mIntent = Intent(this, Nickname::class.java)
//            startActivity(mIntent)
        }

        binding.flMypagePwBackbtn.setOnClickListener {
            finish()
        }

        binding.etMypagePwPw.addTextChangedListener(createTextWatcher(::checkPwFormat))
        binding.etMypagePwRepw.addTextChangedListener(createTextWatcher(::checkRePw))
        binding.etMypagePwMypw.addTextChangedListener(createTextWatcher(::checkMyPw))
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

    private fun checkMyPw() {
        val myPw = "a1234567"
        var inputMyPW = binding.etMypagePwMypw.text.toString()
        if (inputMyPW.equals(myPw)) {
            binding.etMypagePwMypw.background = resources.getDrawable(R.drawable.bg_gray_ed_fill_6_rect)
            binding.tvMypagePwMypwCheck.visibility = View.INVISIBLE
            binding.ivMypagePwPwCheck0.visibility = View.VISIBLE
            flag = 1
        } else {
            binding.etMypagePwMypw.background = resources.getDrawable(R.drawable.bg_gray_ed_fill_6_rect_border_red)
            binding.tvMypagePwMypwCheck.visibility = View.VISIBLE
            binding.ivMypagePwPwCheck0.visibility = View.INVISIBLE
            flag = 0
        }
    }
    private fun checkPwFormat() {
        var pw = binding.etMypagePwPw.text.toString()
        if (SignUtils.isValidPassword(pw) == true) {
            binding.etMypagePwPw.background = resources.getDrawable(R.drawable.bg_gray_ed_fill_6_rect)
            binding.tvMypagePwCheck1.setTextColor(Color.parseColor("#99808080"))
        } else {
            binding.etMypagePwPw.background = resources.getDrawable(R.drawable.bg_gray_ed_fill_6_rect_border_red)
            binding.tvMypagePwCheck1.setTextColor(Color.parseColor("#CB443D"))
        }
    }
    private fun checkRePw() {
        var pw0 = binding.etMypagePwPw.text.toString()
        var pw1 = binding.etMypagePwRepw.text.toString()

        if (pw0.equals(pw1)) {
            binding.etMypagePwRepw.background = resources.getDrawable(R.drawable.bg_gray_ed_fill_6_rect)

            binding.tvMypagePwCheck2.visibility = View.INVISIBLE
            binding.ivMypagePwCheck1.visibility = View.VISIBLE
            binding.ivMypagePwCheck2.visibility = View.VISIBLE

            binding.btnMypagePwNext.isEnabled = flag == 1
        } else {
            binding.etMypagePwRepw.background = resources.getDrawable(R.drawable.bg_gray_ed_fill_6_rect_border_red)

            binding.tvMypagePwCheck2.visibility = View.VISIBLE
            binding.ivMypagePwCheck1.visibility = View.INVISIBLE
            binding.ivMypagePwCheck2.visibility = View.INVISIBLE

            binding.btnMypagePwNext.isEnabled = false
        }
    }
}
