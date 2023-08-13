package com.umc.yourweather.data.service

import com.umc.yourweather.data.remote.request.ChangePasswordRequest
import com.umc.yourweather.data.remote.request.SignupRequest
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.TokenResponse
import com.umc.yourweather.data.remote.response.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserService {

    // 회원 탈퇴
    @PUT("/api/v1/users/withdraw")
    fun withdrawUser(): Call<BaseResponse<UserResponse>>

    // 회원 가입
    @POST("/api/v1/users/signup")
    fun signUp(@Body request: SignupRequest): Call<BaseResponse<TokenResponse>>

    // 비밀번호 변경
    @POST("/api/v1/users/password")
    fun changePw(@Body request: ChangePasswordRequest): Call<BaseResponse<UserResponse>>

    // 마이페이지
    @GET("/api/v1/users/mypage")
    fun getMyPage(): Call<BaseResponse<UserResponse>>
}
