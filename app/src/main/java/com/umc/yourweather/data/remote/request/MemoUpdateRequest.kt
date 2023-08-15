package com.umc.yourweather.data.remote.request

import com.google.gson.annotations.SerializedName
import com.umc.yourweather.data.enums.Status

data class MemoUpdateRequest(
    @SerializedName("status") val status: Status,
    @SerializedName("temperature") val temperature: Int,
    @SerializedName("content") val content: String,
)
