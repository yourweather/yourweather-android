package com.umc.yourweather.presentation.sign

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.databinding.ActivityFindPwBinding

class FindPw : AppCompatActivity() {
    lateinit var binding: ActivityFindPwBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindPwBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnFindpwNext.setOnClickListener {
            val mIntent = Intent(this, FindPwEmail::class.java)
            startActivity(mIntent)
        }
        binding.btnFindpwBack.setOnClickListener {
            finish()
        }
    }
}
