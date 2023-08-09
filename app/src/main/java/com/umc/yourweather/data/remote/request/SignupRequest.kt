package com.umc.yourweather.data.remote.request

import com.google.gson.annotations.SerializedName

class SignupRequest(
    @SerializedName("signup_email")
    val email: String,
    @SerializedName("signup_password")
    val password: String,
    @SerializedName("signup_nickname")
    val nickname: String,
    @SerializedName("signup_platform")
    val platform: String,
) {
    enum class platformList {
        KAKAO,
        GOOGLE,
        NAVER,
        YOURWEATHER,
    }
}
