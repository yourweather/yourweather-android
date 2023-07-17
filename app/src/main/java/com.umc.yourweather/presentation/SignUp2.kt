package com.umc.yourweather.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.databinding.ActivitySignUp2Binding

class SignUp2 : AppCompatActivity() {
    lateinit var binding: ActivitySignUp2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUp2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignup2Signup.setOnClickListener {
            val mIntent = Intent(this, Nickname::class.java)
            startActivity(mIntent)
            finish()
        }
    }
}
