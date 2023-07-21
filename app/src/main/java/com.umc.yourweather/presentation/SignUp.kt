package com.umc.yourweather.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
            finish()
        }

        binding.tvSignupBtnsignin.setOnClickListener{
            val mIntent = Intent(this, SignUp2::class.java)
            startActivity(mIntent)
            finish()
        }

    }
}