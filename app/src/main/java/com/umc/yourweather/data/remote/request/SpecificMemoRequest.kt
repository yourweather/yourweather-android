package com.umc.yourweather.data.remote.request

import com.google.gson.annotations.SerializedName

class SpecificMemoRequest(
    @SerializedName("month")
    val email: Int,
    @SerializedName("weather")
    val password: String,
)
