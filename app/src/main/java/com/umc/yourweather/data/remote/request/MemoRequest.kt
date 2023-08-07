package com.umc.yourweather.data.remote.request

import com.google.gson.annotations.SerializedName

data class MemoRequest(
    @SerializedName("memo_status")
    val status: Status,
    @SerializedName("memo_content")
    val content: String,
    @SerializedName("memo_localDateTime")
    val localDateTime: String?,
    @SerializedName("memo_temperature")
    val temperature: Int?,
) {
    enum class Status {
        SUNNY,
        CLOUDY,
        RAINY,
        LIGHTNING,
    }
}
