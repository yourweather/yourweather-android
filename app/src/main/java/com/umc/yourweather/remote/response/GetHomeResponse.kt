package com.umc.yourweather.remote.response

data class GetHomeResponse(
    val success: Boolean,
    val code: Int,
    val message: String,
    val result: HomeResponse
) {
    data class HomeResponse(
        val nickname: String,
        val status: Status,
        val temperature: Int
    ) {
        enum class Status {
            SUNNY,
            CLOUDY,
            RAINY,
            LIGHTNING
        }
    }
}
