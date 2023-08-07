package com.umc.yourweather.data.remote.response

class GetMissedInputResponse (
    val success: Boolean,
    val code: Int,
    val message: String,
    val result: com.umc.yourweather.data.remote.response.GetMissedInputResponse.MissedInputResponse
) {
    data class MissedInputResponse(
        val localDates: List<String>
    )
}
