package com.umc.yourweather.data.remote.request

import com.umc.yourweather.data.enums.Status

data class MemoUpdateRequest(
    val status: Status,
    val temperature: Int,
    val content: String,
)
