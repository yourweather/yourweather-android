package com.umc.yourweather.data.service

import com.umc.yourweather.data.remote.response.BaseResponse
import retrofit2.Call
import retrofit2.http.GET

interface PolicyService {

    // 이용약관
    @GET("/api/v1/policy/terms-of-use")
    fun policyUse(): Call<BaseResponse<String>>

    // 개인정보 처리방침
    @GET("/api/v1/policy/privacy-policy")
    fun policyPrivacy(): Call<BaseResponse<String>>
}