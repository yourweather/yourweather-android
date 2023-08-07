package com.umc.yourweather.remote.response

data class GetMemoUpdateResponse(
    val success: Boolean,
    val code: Int,
    val message: String,
    val result: MemoUpdateResponse
) {
    data class MemoUpdateResponse(
        val status: Status,
        val temperature: Int,
        val content: String
    ) {
        enum class Status {
            SUNNY,
            CLOUDY,
            RAINY,
            LIGHTNING
        }
    }
}
