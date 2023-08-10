package com.umc.yourweather.data.remote.request

import com.google.gson.annotations.SerializedName

class SignupRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("platform")
    val platform: String,
)
