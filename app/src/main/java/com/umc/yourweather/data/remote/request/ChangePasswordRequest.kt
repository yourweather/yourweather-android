package com.umc.yourweather.data.remote.request

import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest(
    @SerializedName("password")
    val password: String,
    @SerializedName("newPassword")
    val newPassword: String
)
