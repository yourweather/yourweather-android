package com.umc.yourweather.data.remote.response

data class HomeResponse(
    val nickname: String,
    val status: com.umc.yourweather.data.remote.response.HomeResponse.Status,
    val temperature: Int,
) {
    enum class Status {
        SUNNY,
        CLOUDY,
        RAINY,
        LIGHTNING,
    }
}
