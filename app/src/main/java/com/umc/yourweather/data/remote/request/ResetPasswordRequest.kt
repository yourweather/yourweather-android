package com.umc.yourweather.data.remote.request

import com.google.gson.annotations.SerializedName

data class ResetPasswordRequest(
    @SerializedName("password")
    val password: String
)
