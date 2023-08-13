package com.umc.yourweather.data.remote.response

import com.google.gson.annotations.SerializedName

data class StatisticResponse(
    @SerializedName("sunny")
    val sunny: Float,
    @SerializedName("cloudy")
    val cloudy: Float,
    @SerializedName("rainy")
    val rainy: Float,
    @SerializedName("lightning")
    val lightning: Float
)
