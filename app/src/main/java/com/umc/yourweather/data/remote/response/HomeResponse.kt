package com.umc.yourweather.data.remote.response

import com.umc.yourweather.data.enums.Status

data class HomeResponse(
    val nickname: String,
    val status: Status,
    val temperature: Int,
)
