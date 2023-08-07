package com.umc.yourweather.data.remote.response

data class GetHomeResponse(
    val success: Boolean,
    val code: Int,
    val message: String,
    val result: com.umc.yourweather.data.remote.response.GetHomeResponse.HomeResponse
) {
    data class HomeResponse(
        val nickname: String,
        val status: com.umc.yourweather.data.remote.response.GetHomeResponse.HomeResponse.Status,
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
