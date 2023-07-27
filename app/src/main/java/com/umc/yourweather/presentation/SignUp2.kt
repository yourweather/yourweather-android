package com.umc.yourweather.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivitySignUp2Binding
import com.umc.yourweather.databinding.ActivitySignUpBinding

class SignUp2 : AppCompatActivity() {
    lateinit var binding : ActivitySignUp2Binding
    private var passwordFlag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUp2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignup2Signup.setOnClickListener{
            val mIntent = Intent(this, Nickname::class.java)
            startActivity(mIntent)
            finish()
        }
    }
}