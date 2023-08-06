package com.umc.yourweather.remote.response

data class GetWeatherResponse(
    val success: Boolean,
    val code: Int,
    val message: String,
    val result: WeatherResponse
) {
    data class WeatherResponse(
        val date: String,
        val user: String
    )
}

