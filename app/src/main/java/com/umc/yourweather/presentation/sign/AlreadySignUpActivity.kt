package com.umc.yourweather.presentation.sign

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.R
import com.umc.yourweather.data.enums.PlatformType
import com.umc.yourweather.databinding.ActivityAlreadySignUpBinding

class AlreadySignUpActivity : AppCompatActivity() {
    lateinit var alreadySignUpActivityBinding: ActivityAlreadySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        alreadySignUpActivityBinding = ActivityAlreadySignUpBinding.inflate(layoutInflater)
        setContentView(alreadySignUpActivityBinding.root)

        val platform = intent.getStringExtra("PLATFORM")
        var email = intent.getStringExtra("EMAIL")

        if (email != null) {
            email = email.substring(0, 4) + "*".repeat(email.length - 4)
            alreadySignUpActivityBinding.tvAlreadySignUpEmail.text = email
        }

        var platformImageResId = R.drawable.img_yourweatherlogo
        var platformName = PlatformType.YOURWEATHER.toString()

        when (platform) {
            "KAKAO" -> {
                platformImageResId = R.drawable.ic_signin_kakao
                platformName = "카카오"
            }
            "GOOGLE" -> {
                platformImageResId = R.drawable.ic_signin_google
                platformName = "구글"
            }
            "NAVER" -> {
                platformImageResId = R.drawable.ic_signin_naver
                platformName = "네이버"
            }
            "YOURWEATHER" -> {
                platformImageResId = R.drawable.img_yourweatherlogo
            }
            else -> {
                val mIntent = Intent(this@AlreadySignUpActivity, SignInActivity::class.java)
                startActivity(mIntent)
                finish()
            }
        }
        alreadySignUpActivityBinding.ivAlreadySignUpLogo.setImageResource(platformImageResId)
        alreadySignUpActivityBinding.tvAlreadySignUpPlatform.text = platformName

        alreadySignUpActivityBinding.btnAlreadySignUpEmailSignIn.setOnClickListener {
            finish()
        }
    }
}
