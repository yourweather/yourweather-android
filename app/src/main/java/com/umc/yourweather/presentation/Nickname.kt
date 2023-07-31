package com.umc.yourweather.presentation

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.R
import com.umc.yourweather.util.NicknameUtils.Companion.getRandomHintText

class Nickname : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nickname)

        val editText: EditText = findViewById(R.id.et_nickname_nickname)
        val button: Button = findViewById(R.id.btn_nickname_refresh)

        // btn 클릭할 때마다 랜덤 닉네임 추천
        button.setOnClickListener {
            editText.setText("")
            editText.hint = getRandomHintText()
        }
    }
}
