package com.umc.yourweather.presentation

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.R

class Nickname : AppCompatActivity() {

    // RandomName리스트
    private val hintList = listOf(
        "맑은 비구름", "눈부신 번개", "맑은 번개", "비온 뒤 맑음", "비온 뒤 햇살",
        "신난 번개", "웃는 비", "걸어가는 구름", "맑은 안개", "안개",
        "맑음", "구름 조금", "약한 비", "비와서 좋아", "우루르쾅 번개",
        "쨍쨍 햇살", "강아지 구름", "양떼구름", "눈구름", "번개구름",
        "새털구름", "토네이도", "구르름", "이슬이슬비", "가랑비",
        "소나기", "폭우", "장마", "천둥번개", "두둥번개",
        "우산 쓴 번개", "빠지직번개", "보슬비", "하늘구름", "비온 뒤 봄", "비온 뒤 화창",
    )

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

    private fun getRandomHintText(): String {
        val randomIndex = (0 until hintList.size).random()
        return hintList[randomIndex]
    }
}
