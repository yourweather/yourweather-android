package com.umc.yourweather.remote.response

data class HomeResponse(
    val nickname: String,
    val status: Status,
    val temperature: Int,
) {
    enum class Status {
        SUNNY,
        CLOUDY,
        RAINY,
        LIGHTNING,
    }
}
