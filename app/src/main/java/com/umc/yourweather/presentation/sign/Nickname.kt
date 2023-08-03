package com.umc.yourweather.presentation.sign

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivityNicknameBinding
import com.umc.yourweather.presentation.BottomNavi
import com.umc.yourweather.util.NicknameUtils.Companion.getRandomHintText

class Nickname : AppCompatActivity() {

    lateinit var binding: ActivityNicknameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNicknameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val editText: EditText = findViewById(R.id.et_nickname_nickname)
        val button: Button = findViewById(R.id.btn_nickname_refresh)

        // btn 클릭할 때마다 랜덤 닉네임 추천
        button.setOnClickListener {
            editText.setText("")
            editText.hint = getRandomHintText()
        }

        binding.btnNicknameNext.setOnClickListener {
            val mIntent = Intent(this, BottomNavi::class.java)
            startActivity(mIntent)
        }
    }
}
