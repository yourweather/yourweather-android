package com.umc.yourweather.presentation.sign

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.user.UserApiClient
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivitySocialSignCheckBinding
import com.umc.yourweather.util.SignUtils

class SocialSignCheck : AppCompatActivity() {
    lateinit var binding: ActivitySocialSignCheckBinding
    var userEmail: String? = null
    var userPw: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySocialSignCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val flag = intent.getStringExtra("SIGN")

        if (flag.equals("KAKAO")) {
            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    Log.d(SignUtils.KAKAOTAG, "사용자 정보 요청 실패", error)
                } else if (user != null) {
                    setEmail(user.id.toString(), user.kakaoAccount?.email)
                }
            }
//        } else if (flag.equals("NAVER")) {
//            NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
//                override fun onSuccess(result: NidProfileResponse) {
//                    // email = result.profile?.email.toString()
//                    // result.profile?.id
//                    setEmail(result.profile?.email.toString(), result.profile?.id)
//                    Log.d(SignUtils.NAVERTAG, "네이버 로그인 : ${result.profile?.email}")
//                    Log.d(SignUtils.NAVERTAG, "네이버 로그인한 유저 정보 - 이메일 : ${result.profile?.id}")
//                }
//
//                override fun onError(errorCode: Int, message: String) {
//                    //
//                }
//
//                override fun onFailure(httpStatus: Int, message: String) {
//                    //
//                }
//            })
        }

        binding.button5.setOnClickListener {
            val mIntent = Intent(this@SocialSignCheck, SignIn::class.java)
            startActivity(mIntent)
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.d(SignUtils.KAKAOTAG, "카카오 로그아웃 실패")
                } else {
                    Log.d(SignUtils.KAKAOTAG, "카카오 로그아웃 성공!")
                }
            }
            finish()
        }
    }

    fun setEmail(email: String?, pw: String?) {
        binding.tvSocialsigncheckEmail.text = email
        binding.tvSocialsigncheckPw.text = pw
    }
}
