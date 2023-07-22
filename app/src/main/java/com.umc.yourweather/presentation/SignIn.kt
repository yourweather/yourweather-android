package com.umc.yourweather.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivitySignInBinding
import com.umc.yourweather.databinding.ActivitySignUpBinding

class SignIn : AppCompatActivity() {
    lateinit var binding : ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvSigninBtnfindpw.setOnClickListener{
            val mIntent = Intent(this, FindPw::class.java)
            startActivity(mIntent)
        }

        binding.tvSigninBtnsignup.setOnClickListener{
            val mIntent = Intent(this, SignUp::class.java)
            startActivity(mIntent)
        }

    }
}