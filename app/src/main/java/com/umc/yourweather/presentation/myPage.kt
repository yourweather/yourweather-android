package com.umc.yourweather.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.databinding.ActivityMyPageBinding

class myPage : AppCompatActivity() {
    lateinit var binding: ActivityMyPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        binding.apply {
            alarm.setOnClickListener {
                val i = Intent(this@myPage, my_alarm::class.java)
                startActivity(i)
            }
            myInfo.setOnClickListener {
                val i = Intent(this@myPage, my_info::class.java)
                startActivity(i)
            }
//            eventWeather.setOnClickListener {
//                val i = Intent(this@myPage, EventWeather::class.java)
//                startActivity(i)
//            }
        }
    }
}
