package com.umc.yourweather.data.remote.request

import com.umc.yourweather.data.enums.Status

data class MemoRequest(
    val status: Status,
    val content: String?,
    val localDateTime: String?,
    val temperature: Int?,
)
