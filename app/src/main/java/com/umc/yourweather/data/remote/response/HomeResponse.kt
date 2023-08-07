package com.umc.yourweather.data.remote.response

import com.google.gson.annotations.SerializedName

data class HomeResponse(
    @SerializedName("home_nickname")
    val nickname: String,
    @SerializedName("home_status")
    val status: Status,
    @SerializedName("home_temperature")
    val temperature: Int,
) {
    enum class Status {
        SUNNY,
        CLOUDY,
        RAINY,
        LIGHTNING,
    }
}
