package com.umc.yourweather.presentation.mypage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.umc.yourweather.R

class WithdrawStatusbar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_withdraw_statusbar)
        val toolbar: Toolbar = findViewById(R.id.tb_withdraw_back)
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로 가기 버튼 활성화
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_calendardetailview_back)
    }
}