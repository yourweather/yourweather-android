package com.umc.yourweather.data.remote.response

data class MemoUpdateResponse(
    val status: com.umc.yourweather.data.remote.response.MemoUpdateResponse.Status,
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
