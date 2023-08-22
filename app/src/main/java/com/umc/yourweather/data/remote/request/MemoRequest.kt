package com.umc.yourweather.data.remote.request

import com.umc.yourweather.data.enums.Status

data class MemoRequest(
    val status: Status,
    val content: String?,
    val localDateTime: Any,
    val temperature: Int?,
)
