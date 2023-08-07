package com.umc.yourweather.data.remote.response

import com.google.gson.annotations.SerializedName

data class StatisticResponse(
    @SerializedName("statistic_sunny")
    val sunny: Float,
    @SerializedName("statistic_cloudy")
    val cloudy: Float,
    @SerializedName("statistic_rainy")
    val rainy: Float,
    @SerializedName("statistic_lightning")
    val lightning: Float
)
