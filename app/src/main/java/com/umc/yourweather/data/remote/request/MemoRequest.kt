package com.umc.yourweather.data.remote.request

data class MemoRequest(
    val status: Status,
    val content: String,
    val localDateTime: String?,
    val temperature: Int?,
) {
    enum class Status {
        SUNNY,
        CLOUDY,
        RAINY,
        LIGHTNING,
    }
}
