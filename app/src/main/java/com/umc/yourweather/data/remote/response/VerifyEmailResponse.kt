package com.umc.yourweather.data.remote.response


import com.google.gson.annotations.SerializedName

data class VerifyEmailResponse(
    @SerializedName("auth")
    val auth: Boolean,
    @SerializedName("platform")
    val platform: String
)