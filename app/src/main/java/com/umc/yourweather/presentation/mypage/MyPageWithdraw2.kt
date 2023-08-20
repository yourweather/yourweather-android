package com.umc.yourweather.presentation.mypage

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.umc.yourweather.R
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.UserResponse
import com.umc.yourweather.data.service.UserService
import com.umc.yourweather.databinding.ActivityMyPageWithdraw2Binding
import com.umc.yourweather.di.App
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.di.UserSharedPreferences
import com.umc.yourweather.presentation.sign.SignIn
import com.umc.yourweather.util.AlertDialogTwoBtn
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageWithdraw2 : AppCompatActivity() {

    private lateinit var binding: ActivityMyPageWithdraw2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageWithdraw2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvWithdraw2Guide2.text =
            "소중한 의견을 들려주세요." +
            "\n${UserSharedPreferences.getUserNickname(this@MyPageWithdraw2)} 님이 유어웨더로 다시 돌아올 수 있도록 더 발전해볼게요."
        binding.btnWithdraw2Withdraw.setOnClickListener {
            AlertDialogTwoBtn(this).run {
                setTitle("정말 유어웨더 회원을 탈퇴하시겠습니까?")

                setNegativeButton(
                    "취소",
                    object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            dismiss()
                        }
                    },
                )
                setPositiveButton(
                    "탈퇴하기",
                    object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            withdrawApi()
                            dismiss()
                        }
                    },
                )
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                show()
            }
        }

        binding.radioWithdraw2Grp.setOnCheckedChangeListener { group, checkedId ->
            // 선택한 라디오 버튼이 하나라도 있을 때 저장 버튼 활성화
            val isAnyRadioButtonSelected = checkedId != -1
            binding.btnWithdraw2Withdraw.isEnabled = isAnyRadioButtonSelected

            val textColorRes = if (isAnyRadioButtonSelected) R.color.sorange else R.color.white
            binding.btnWithdraw2Withdraw.setTextColor(ContextCompat.getColor(this, textColorRes))
        }

        binding.btnWithdraw2Cancel.setOnClickListener {
            finish()
        }
    }

//    @PUT("/api/v1/users/withdraw")
//    fun withdrawUser(): Call<BaseResponse<UserResponse>>
    fun withdrawApi() {
        val service = RetrofitImpl.authenticatedRetrofit.create(UserService::class.java)

        service.withdrawUser().enqueue(object : Callback<BaseResponse<UserResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<UserResponse>>,
                response: Response<BaseResponse<UserResponse>>,
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val code = responseBody?.code
                    if (code == 200) {
                        // 토큰 없앰
                        App.token_prefs.clearTokens()
                        // SH의 모든 정보 없앰..
                        UserSharedPreferences.clearUser(this@MyPageWithdraw2)

                        Log.d("WithDrawDebug", "회원탈퇴 성공")
                        val intent = Intent(this@MyPageWithdraw2, MyPageWithdarw3::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    Log.d("WithDrawDebug", "onResponse")
                }
            }

            override fun onFailure(call: Call<BaseResponse<UserResponse>>, t: Throwable) {
                Log.d("WithDrawDebug", "onFailure")
                val mIntent = Intent(this@MyPageWithdraw2, SignIn::class.java)
                startActivity(mIntent)
                finish()
            }
        })
    }
}
