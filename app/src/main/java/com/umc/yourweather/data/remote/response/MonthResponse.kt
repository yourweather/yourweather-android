package com.umc.yourweather.data.remote.response

import com.google.gson.annotations.SerializedName

data class MonthResponse(
    @SerializedName("weatherList")
    val weatherList: List<MonthWeatherResponse>,
){
    data class MonthWeatherResponse(
        @SerializedName("date")
        val date: String,
        @SerializedName("lastStatus")
        val lastStatus: String,
        @SerializedName("lastTemperature")
        val lastTemperature: Int,
        @SerializedName("weatherId")
        val weatherId: Int,
    )
}