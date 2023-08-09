package com.umc.yourweather.data.service

import com.umc.yourweather.data.remote.request.LoginRequest
import com.umc.yourweather.data.remote.response.TokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {

    // 일반 로그인
    @POST("api/v1/users/login")
    fun logIn(@Body request: LoginRequest): Call<TokenResponse>

    // 소셜 로그인
    @POST("api/v1/users/oauth-login")
    fun oauthLogIn(@Body request: LoginRequest): Call<TokenResponse>
}
