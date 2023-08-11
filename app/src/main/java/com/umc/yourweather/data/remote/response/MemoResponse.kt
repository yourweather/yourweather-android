package com.umc.yourweather.data.remote.response

import com.google.gson.annotations.SerializedName

data class MemoResponse(
    @SerializedName("status")
    val status: Status,
    @SerializedName("content")
    val content: String,
    @SerializedName("localDateTime")
    val localDateTime: String?,
    @SerializedName("temperature")
    val temperature: Int?,
) {
    enum class Status {
        SUNNY,
        CLOUDY,
        RAINY,
        LIGHTNING,
    }
}
