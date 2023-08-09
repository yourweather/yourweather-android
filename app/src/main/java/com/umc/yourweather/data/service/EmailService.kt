package com.umc.yourweather.data.service

import com.umc.yourweather.data.remote.response.BaseResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EmailService {

    // 이메일 전송
    @GET("/api/v1/email/send")
    fun sendEmail(
        @Query("email") email: String,
    ): Call<BaseResponse<Unit>>

    // 이메일 인증
    @GET("/api/v1/email/certify")
    fun certifyEmail(
        @Query("email") email: String,
        @Query("code") code: String,
    ): Call<BaseResponse<Boolean>>
}
