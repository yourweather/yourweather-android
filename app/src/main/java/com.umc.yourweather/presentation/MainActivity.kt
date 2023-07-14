
package com.umc.yourweather.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMain.setOnClickListener{
            val mIntent = Intent(this, SocialSignUp::class.java)
            startActivity(mIntent)
            finish()
        }
    }
}
