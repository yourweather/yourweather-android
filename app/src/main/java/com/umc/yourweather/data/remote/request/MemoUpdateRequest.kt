package com.umc.yourweather.data.remote.request

data class MemoUpdateRequest(
    val status: com.umc.yourweather.data.remote.request.MemoUpdateRequest.Status,
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
