package com.umc.yourweather.data.remote.response

data class GetStatisticResponse(
    val success: Boolean,
    val code: Int,
    val message: String,
    val result: com.umc.yourweather.data.remote.response.GetStatisticResponse.StatisticResponse
) {
    data class StatisticResponse(
        val sunny: Float,
        val cloudy: Float,
        val rainy: Float,
        val lightning: Float
    )
}
