package com.umc.yourweather.data.remote.response

import com.google.gson.annotations.SerializedName
import com.umc.yourweather.data.enums.Status

data class MemoUpdateResponse(
    @SerializedName("status")
    val status: Status,
    @SerializedName("temperature")
    val temperature: Int,
    @SerializedName("content")
    val content: String,
)
