package com.umc.yourweather.presentation.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivityMyInfoBinding
import com.umc.yourweather.di.MySharedPreferences
import com.umc.yourweather.di.TokenSharedPreferences
import com.umc.yourweather.presentation.sign.SignIn

class MyPageMyInfo : AppCompatActivity() {
    lateinit var binding: ActivityMyInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.flMyinfoBackbtn.setOnClickListener {
            finish()
        }

        binding.btnMyinfoPwChange.setOnClickListener {
            val mIntent = Intent(this, MyPagePwChange::class.java)
            startActivity(mIntent)
        }

        val nickname = intent.getStringExtra("nickname")
        val email = intent.getStringExtra("email")
        val platform = intent.getStringExtra("platform")

        binding.tvMyinfoNickname.text = nickname
        binding.tvMyinfoEmail.text = email

        binding.btnMyinfoNicknameChange.setOnClickListener {
            val mIntent = Intent(this, MyPageNicknameChange::class.java)
            mIntent.putExtra("nickname", nickname)

            startActivity(mIntent)
        }
        binding.tvMyinfoLogout.setOnClickListener {
            // 기기에 저장된 비밀번호와 이메일 정보 삭제
            MySharedPreferences.clearUser(this)
            val tokenPrefs = TokenSharedPreferences(this)
            tokenPrefs.clearTokens()

            // 로그인 창으로 이동
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
            finish()
        }

        if (platform != "YOURWEATHER") {
            binding.tvMyinfoPw.text = "SNS 계정에서 변경하실 수 있습니다."
            binding.btnMyinfoPwChange.visibility = View.GONE
            binding.ivMyinfoSocialIc.visibility = View.VISIBLE
            if (platform == "GOOGLE") {
                binding.ivMyinfoSocialIc.setImageResource(R.drawable.ic_signin_naver)
            }
            if (platform == "KAKAO") {
                binding.ivMyinfoSocialIc.setImageResource(R.drawable.ic_signin_kakao)
            }
            if (platform == "NAVER") {
                binding.ivMyinfoSocialIc.setImageResource(R.drawable.ic_signin_naver)
            }
        }
    }
}
