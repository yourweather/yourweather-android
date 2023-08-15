package com.umc.yourweather.data.service

import com.umc.yourweather.data.remote.request.ChangeNicknameRequest
import com.umc.yourweather.data.remote.request.ChangePasswordRequest
import com.umc.yourweather.data.remote.request.ResetPasswordRequest
import com.umc.yourweather.data.remote.request.SignupRequest
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.ChangePasswordRespond
import com.umc.yourweather.data.remote.response.TokenResponse
import com.umc.yourweather.data.remote.response.UserResponse
import com.umc.yourweather.data.remote.response.VerifyEmailResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface UserService {

    // 회원 탈퇴
    @PUT("/api/v1/users/withdraw")
    fun withdrawUser(): Call<BaseResponse<UserResponse>>

    // 회원 가입
    @POST("/api/v1/users/signup")
    fun signUp(@Body request: SignupRequest): Call<BaseResponse<TokenResponse>>

    // 비밀번호 변경
    @PATCH("/api/v1/users/password-change")
    fun changePw(@Body request: ChangePasswordRequest): Call<BaseResponse<ChangePasswordRespond>>

    // 비밀번호 재설정
    @POST("/api/v1/users/password-reset")
    fun changePw(@Body request: ResetPasswordRequest): Call<BaseResponse<UserResponse>>

    // 닉네임 변경
    @PATCH("/api/v1/users/nickname")
    fun changeNickname(@Body request: ChangeNicknameRequest): Call<BaseResponse<UserResponse>>

    // 마이페이지
    @GET("/api/v1/users/mypage")
    fun getMyPage(): Call<BaseResponse<UserResponse>>

    // 비번찾기 이메일 인증
    @GET("/api/v1/users/verify-user-email")
    fun verifyEmail(@Query("email") request: String): Call<BaseResponse<VerifyEmailResponse>>
}
