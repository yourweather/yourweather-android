package com.umc.yourweather.remote.response

class GetMissedInputResponse (
    val success: Boolean,
    val code: Int,
    val message: String,
    val result: MissedInputResponse
) {
    data class MissedInputResponse(
        val localDates: List<String>
    )
}
