package com.umc.yourweather.remote.response

data class MemoResponse(
    val status: Status,
    val content: String,
    val localDateTime: String?,
    val temperature: Int?
) {
    enum class Status {
        SUNNY,
        CLOUDY,
        RAINY,
        LIGHTNING
    }
}
