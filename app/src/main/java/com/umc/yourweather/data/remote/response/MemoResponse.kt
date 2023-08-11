package com.umc.yourweather.data.remote.response

import com.google.gson.annotations.SerializedName
import com.umc.yourweather.data.enums.Status

data class MemoResponse(
    @SerializedName("status")
    val status: Status,
    @SerializedName("content")
    val content: String,
    @SerializedName("localDateTime")
    val localDateTime: String?,
    @SerializedName("temperature")
    val temperature: Int?,
)
