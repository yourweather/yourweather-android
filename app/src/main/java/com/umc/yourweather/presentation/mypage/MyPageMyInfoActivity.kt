package com.umc.yourweather.presentation.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.R
import com.umc.yourweather.data.enums.PlatformType
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.UserResponse
import com.umc.yourweather.data.service.UserService
import com.umc.yourweather.databinding.ActivityMyInfoBinding
import com.umc.yourweather.di.RetrofitImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageMyInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityMyInfoBinding

    override fun onResume() {
        super.onResume()
        // 현재 닉네임 보여주기 위한 API
        userInfoAPI()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.flMyinfoBackbtn.setOnClickListener {
            finish()
        }

        binding.btnMyinfoBack.setOnClickListener {
            finish()
        }

        binding.btnMyinfoPwChange.setOnClickListener {
            val mIntent = Intent(this, MyPagePwChangeActivity::class.java)
            startActivity(mIntent)
        }

        val nickname = intent.getStringExtra("nickname")
        val email = intent.getStringExtra("email")
        val platform = intent.getStringExtra("platform")

        binding.tvMyinfoNickname.text = nickname

        binding.btnMyinfoNicknameChange.setOnClickListener {
            val mIntent = Intent(this, MyPageNicknameChangeActivity::class.java)
            mIntent.putExtra("nickname", nickname)

            startActivity(mIntent)
        }

        // 로그아웃
        binding.tvMyinfoLogout.setOnClickListener {
            // alertdialog_mypage_logout 프래그먼트로 이동
            val fragment = MyPageLogoutFragment()
            val fragmentManager = supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.total_View, fragment)
            transaction.addToBackStack(null)
            transaction.commit()

//            // 기기에 저장된 비밀번호와 이메일 정보 삭제
//            MySharedPreferences.clearUser(this)
//            val tokenPrefs = TokenSharedPreferences(this)
//            tokenPrefs.clearTokens()
//
//            // 로그인 창으로 이동
//            val intent = Intent(this, SignIn::class.java)
//            startActivity(intent)
//            finish()
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

        // 회원탈퇴
        binding.tvMyinfoBtnWithdraw.setOnClickListener {
            val mIntent = Intent(this, MyPageWithdraw2::class.java)
            startActivity(mIntent)
        }
    }

    private fun userInfoAPI() {
        val service = RetrofitImpl.authenticatedRetrofit.create(UserService::class.java)
        val call = service.getMyPage()

        call.enqueue(object : Callback<BaseResponse<UserResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<UserResponse>>,
                response: Response<BaseResponse<UserResponse>>,
            ) {
                if (response.isSuccessful) {
                    val baseResponse = response.body()
                    if (baseResponse != null) {
                        val userResponse = baseResponse.result
                        // 받아온 userResponse 객체를 활용하여 뷰에 데이터를 설정하고 아이콘을 표시
                        if (userResponse != null) {
                            binding.tvMyinfoNickname.text = "${userResponse.nickname} 님"
                            binding.tvMyinfoEmail.text = userResponse.email
                            val platformType = PlatformType.valueOf(userResponse.platform)
                            // 플랫폼 종류에 따라 이미지 설정
                            val platformImageResId = when (platformType) {
                                PlatformType.GOOGLE -> R.drawable.ic_signin_google
                                PlatformType.KAKAO -> R.drawable.ic_signin_kakao
                                PlatformType.NAVER -> R.drawable.ic_signin_naver
                                // 필요한 경우 다른 플랫폼도 추가 가능
                                else -> R.drawable.img_yourweatherlogo
                            }
                        }
                    } else {
                        Log.e("Error (null)", "Response body 비었음")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("마이페이지 API Error", "Response Code: ${response.code()}, Error Body: $errorBody")
                }
            }

            override fun onFailure(call: Call<BaseResponse<UserResponse>>, t: Throwable) {
                Log.e("마이페이지API Failure", "Error: ${t.message}", t)
            }
        })
    }
}
