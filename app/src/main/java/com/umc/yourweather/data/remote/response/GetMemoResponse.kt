package com.umc.yourweather.data.remote.response

data class GetMemoResponse(
    val success: Boolean,
    val code: Int,
    val message: String,
    val result: com.umc.yourweather.data.remote.response.GetMemoResponse.MemoResponse
) {
    data class MemoResponse(
        val status: com.umc.yourweather.data.remote.response.GetMemoResponse.MemoResponse.Status,
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
}

