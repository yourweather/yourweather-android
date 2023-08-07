package com.umc.yourweather.data.remote.response

data class GetMemoUpdateResponse(
    val success: Boolean,
    val code: Int,
    val message: String,
    val result: com.umc.yourweather.data.remote.response.GetMemoUpdateResponse.MemoUpdateResponse
) {
    data class MemoUpdateResponse(
        val status: com.umc.yourweather.data.remote.response.GetMemoUpdateResponse.MemoUpdateResponse.Status,
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
