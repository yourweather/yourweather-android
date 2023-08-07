package com.umc.yourweather.data.remote.response

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("weather_date")
    val date: String,
    @SerializedName("weather_user")
    val user: String
)
