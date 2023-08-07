package com.umc.yourweather.data.remote.response

data class GetWeatherResponse(
    val success: Boolean,
    val code: Int,
    val message: String,
    val result: com.umc.yourweather.data.remote.response.GetWeatherResponse.WeatherResponse
) {
    data class WeatherResponse(
        val date: String,
        val user: String
    )
}

