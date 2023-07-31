package com.umc.yourweather.presentation

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivitySignUp2Binding
import com.umc.yourweather.util.SignUtils.Companion.isValidPassword

class SignUp2 : AppCompatActivity() {
    lateinit var binding: ActivitySignUp2Binding
    private var passwordFlag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUp2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignup2Next.setOnClickListener {
            val mIntent = Intent(this, Nickname::class.java)
            startActivity(mIntent)
        }
        binding.btnSignup2Back.setOnClickListener {
            val mIntent = Intent(this, SignUp::class.java)
            startActivity(mIntent)
            finish()
        }

        binding.etSignup2Pw.addTextChangedListener(createTextWatcher(::checkPwFormat))
        binding.etSignup2Repw.addTextChangedListener(createTextWatcher(::checkRePw))
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
        var pw = binding.etSignup2Pw.text.toString()
        if (isValidPassword(pw) == true) {
            binding.etSignup2Pw.background = resources.getDrawable(R.drawable.bg_gray_ed_fill_6_rect)
            binding.tvSignup2Check1.setTextColor(Color.parseColor("#99808080"))
        } else {
            binding.etSignup2Pw.background = resources.getDrawable(R.drawable.bg_gray_ed_fill_6_rect_border_red)
            binding.tvSignup2Check1.setTextColor(Color.parseColor("#CB443D"))
        }
    }
    private fun checkRePw() {
        var pw0 = binding.etSignup2Pw.text.toString()
        var pw1 = binding.etSignup2Repw.text.toString()

        if (pw0.equals(pw1)) {
            binding.etSignup2Repw.background = resources.getDrawable(R.drawable.bg_gray_ed_fill_6_rect)

            binding.tvSignup2Check2.visibility = View.INVISIBLE
            binding.ivSignup2Check0.visibility = View.VISIBLE
            binding.ivSignup2Check1.visibility = View.VISIBLE

            binding.btnSignup2Next.isEnabled = true
        } else {
            binding.etSignup2Repw.background = resources.getDrawable(R.drawable.bg_gray_ed_fill_6_rect_border_red)

            binding.tvSignup2Check2.visibility = View.VISIBLE
            binding.ivSignup2Check0.visibility = View.INVISIBLE
            binding.ivSignup2Check1.visibility = View.INVISIBLE

            binding.btnSignup2Next.isEnabled = false
        }
    }
}
