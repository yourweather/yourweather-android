package com.umc.yourweather.remote.request

data class MemoUpdateRequest(
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
