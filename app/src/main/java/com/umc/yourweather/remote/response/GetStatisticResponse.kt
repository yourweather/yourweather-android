package com.umc.yourweather.remote.response

data class GetStatisticResponse(
    val success: Boolean,
    val code: Int,
    val message: String,
    val result: StatisticResponse
) {
    data class StatisticResponse(
        val sunny: Float,
        val cloudy: Float,
        val rainy: Float,
        val lightning: Float
    )
}
