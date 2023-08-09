package com.umc.yourweather.data.remote.request


import com.google.gson.annotations.SerializedName

data class LoginRequset(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)