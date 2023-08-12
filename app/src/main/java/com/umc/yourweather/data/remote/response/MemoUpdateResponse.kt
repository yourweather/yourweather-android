package com.umc.yourweather.data.remote.response

import com.google.gson.annotations.SerializedName
import com.umc.yourweather.data.enums.Status

data class MemoUpdateResponse(
    @SerializedName("update_status")
    val status: Status,
    @SerializedName("update_temperature")
    val temperature: Int,
    @SerializedName("update_content")
    val content: String,
)
