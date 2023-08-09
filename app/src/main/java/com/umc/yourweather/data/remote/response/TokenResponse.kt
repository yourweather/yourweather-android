package com.umc.yourweather.data.remote.response

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("Authorization")
    val accessToken: String,

    @SerializedName("Authorization-refresh")
    val refreshToken: String
)
