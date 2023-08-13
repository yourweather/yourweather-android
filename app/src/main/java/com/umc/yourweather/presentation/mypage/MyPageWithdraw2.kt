package com.umc.yourweather.presentation.mypage

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.UserResponse
import com.umc.yourweather.data.service.UserService
import com.umc.yourweather.databinding.ActivityMyPageWithdraw2Binding
import com.umc.yourweather.di.RetrofitImpl
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

        binding.btnWithdraw2Withdraw.setOnClickListener {
            AlertDialogTwoBtn(this).run {
                setNegativeButton(
                    "취소",
                    object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            finish()
                        }
                    },
                )
                setPositiveButton(
                    "탈퇴하기",
                    object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            withdrawApi()
                        }
                    },
                )
                show()
            }
        }
        binding.btnWithdraw2Withdraw
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
            }
        })
    }
}
