package com.umc.yourweather.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("email")
    val email: String
)
