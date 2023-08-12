package com.umc.yourweather.data.remote.response

import com.google.gson.annotations.SerializedName
import com.umc.yourweather.data.enums.Status

data class MonthWeatherResponse(
    @SerializedName("date")
    var date: String,
    @SerializedName("lastStatus")
    val lastStatus: Status,
    @SerializedName("lastTemperature")
    val lastTemperature: Int,
    @SerializedName("weatherId")
    val weatherId: Int,
)
