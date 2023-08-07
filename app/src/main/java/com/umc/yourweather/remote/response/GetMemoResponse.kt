package com.umc.yourweather.remote.response

data class GetMemoResponse(
    val success: Boolean,
    val code: Int,
    val message: String,
    val result: MemoResponse
) {
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
}

