package com.umc.yourweather.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResetPwResponse(
    @SerializedName("password")
    val password: String
)
