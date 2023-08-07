package com.umc.yourweather.data.remote.response

data class MemoResponse(
    val status: com.umc.yourweather.data.remote.response.MemoResponse.Status,
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
