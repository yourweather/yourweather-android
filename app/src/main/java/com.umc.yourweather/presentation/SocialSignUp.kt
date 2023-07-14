package com.umc.yourweather.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivitySignUp2Binding
import com.umc.yourweather.databinding.ActivitySocialSignUpBinding

class SocialSignUp : AppCompatActivity() {
    lateinit var binding : ActivitySocialSignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySocialSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSocialsignupSignup.setOnClickListener{
            val mIntent = Intent(this, SignUp::class.java)
            startActivity(mIntent)
            finish()
        }

        binding.btnSocialsignupSignin.setOnClickListener{
            val mIntent = Intent(this, SignIn::class.java)
            startActivity(mIntent)
            finish()
        }
    }
}