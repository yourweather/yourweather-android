package com.umc.yourweather.remote.response

data class MemoUpdateResponse(
    val status: Status,
    val temperature: Int,
    val content: String,
) {
    enum class Status {
        SUNNY,
        CLOUDY,
        RAINY,
        LIGHTNING,
    }
}
