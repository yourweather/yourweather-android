package com.umc.yourweather.presentation

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivitySignUpBinding

class SignUp : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSingupBack.setOnClickListener {
            finish()
        }

        binding.tvSignupBtnsignin.setOnClickListener {
            val mIntent = Intent(this, SignIn::class.java)
            startActivity(mIntent)
        }
        binding.btnSignupNext.setOnClickListener {
            val mIntent = Intent(this@SignUp, SignUp2::class.java)
            startActivity(mIntent)
        }

        binding.etSignupEmail.addTextChangedListener(createTextWatcher(::checkEmailError))
        binding.etSignupAuth.addTextChangedListener(createTextWatcher(::checkAuth))

        binding.btnSignupSendauth.setOnClickListener {
            showCustomAlertDialog("인증코드가 전송되었습니다.", 0)
        }

        binding.btnSignupCheckauth.setOnClickListener {
            showCustomAlertDialog("인증이 완료되었습니다.", 1)
        }
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

    private fun checkEmailError() {
        var email = binding.etSignupEmail.text.toString()
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){


            binding.btnSignupSendauth.isEnabled = true
        }else{
            binding.btnSignupSendauth.isEnabled = false
        }

    }

    private fun checkAuth() {
        var authCode = binding.etSignupAuth.text.toString()
        binding.btnSignupCheckauth.isEnabled = (authCode.length == 6)
    }

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

        // binding.viewBackgroundView2.visibility = View.VISIBLE
        alertDialog.show()

        titleTextView.text = text

        alertButton.setOnClickListener {
            alertDialog.dismiss()
            when (flag) {
                // 확인 버튼 클릭
                1 -> {
                    binding.btnSignupNext.isEnabled = true
                }
            }
        }

        alertDialog.setOnDismissListener {
            // binding.viewBackgroundView2.visibility = View.INVISIBLE
        }
    }
}
