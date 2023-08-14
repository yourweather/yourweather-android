package com.umc.yourweather.presentation.mypage

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.umc.yourweather.R

class MyPageWithdraw : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page_withdraw)
        // 프래그먼트를 추가하고 초기화
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        // 프래그먼트 객체 생성
        val fragment: Fragment = WithdrawPageFragment()

        // 프래그먼트를 레이아웃 컨테이너에 추가
        fragmentTransaction.add(R.id.fragment_container, fragment)

        // 프래그먼트 트랜잭션 완료
        fragmentTransaction.commit()

        val btnContinue: Button = findViewById(R.id.btn_withdrawContinue)

        btnContinue.setOnClickListener {
            val intent = Intent(this@MyPageWithdraw, MyPageWithdraw2::class.java)
            startActivity(intent)
        }
    }
}
