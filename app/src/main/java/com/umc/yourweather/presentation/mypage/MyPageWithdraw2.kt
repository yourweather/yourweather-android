package com.umc.yourweather.presentation.mypage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivityMyPageWithdraw2Binding

class MyPageWithdraw2 : AppCompatActivity() {

    private lateinit var binding: ActivityMyPageWithdraw2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page_withdraw2)

        // 바인딩 객체 초기화
        binding = ActivityMyPageWithdraw2Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 프래그먼트를 추가하고 초기화
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        // 프래그먼트 객체 생성
        val fragment: Fragment = WithdrawPageFragment()

        // 프래그먼트를 레이아웃 컨테이너에 추가
        fragmentTransaction.add(R.id.fragment_container, fragment)


        // 프래그먼트 트랜잭션 완료
        fragmentTransaction.commit()

        val radioButton1 = binding.radioButton1
        val radioButton2 = binding.radioButton2
        val radioButton3 = binding.radioButton3
        val radioButton4 = binding.radioButton4
        val radioButton5 = binding.radioButton5

        val btnCancel: Button = findViewById(R.id.btn_choiceCancel)

        btnCancel.setOnClickListener {
            val intent = Intent(this@MyPageWithdraw2, MyPageWithdraw::class.java)
            startActivity(intent)
        }

    }
}