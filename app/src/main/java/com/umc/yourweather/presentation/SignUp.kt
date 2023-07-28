package com.umc.yourweather.presentation

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivityMainBinding
import com.umc.yourweather.databinding.ActivitySignUpBinding

class SignUp : AppCompatActivity() {
    lateinit var binding : ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignupNext.setOnClickListener{
            val mIntent = Intent(this, SignUp2::class.java)
            startActivity(mIntent)
        }

        binding.tvSignupBtnsignin.setOnClickListener{
            val mIntent = Intent(this, SignIn::class.java)
            startActivity(mIntent)
        }

        //api 연결시 삭제 예정
        binding.btnSignupSendauth.isEnabled = true
        binding.btnSignupNext.isEnabled = true

        binding.btnSignupSendauth.setOnClickListener{
            showCustomAlertDialog("인증코드가 전송되었습니다. ")
        }
    }



    fun showCustomAlertDialog(text : String) {
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

        //binding.viewBackgroundView2.visibility = View.VISIBLE
        alertDialog.show()

        titleTextView.text = text

        alertButton.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.setOnDismissListener {
            //binding.viewBackgroundView2.visibility = View.INVISIBLE
        }
    }
}