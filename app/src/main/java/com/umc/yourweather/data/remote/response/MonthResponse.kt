package com.umc.yourweather.data.remote.response

import com.google.gson.annotations.SerializedName

data class MonthResponse(
    @SerializedName("weatherList")
    val weatherList: List<MonthWeatherResponse>,
)