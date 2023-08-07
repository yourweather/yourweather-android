package com.umc.yourweather.data.remote.request

import com.google.gson.annotations.SerializedName

data class MemoUpdateRequest(
    @SerializedName("update_status")
    val status: Status,
    @SerializedName("update_temperature")
    val temperature: Int,
    @SerializedName("update_content")
    val content: String,
) {
    enum class Status {
        SUNNY,
        CLOUDY,
        RAINY,
        LIGHTNING,
    }
}
