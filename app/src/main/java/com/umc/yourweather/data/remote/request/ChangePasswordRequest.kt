package com.umc.yourweather.data.remote.request

import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest(
    @SerializedName("change_password")
    val password: String
)
