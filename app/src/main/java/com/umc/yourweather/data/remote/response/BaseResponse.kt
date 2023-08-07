package com.umc.yourweather.data.remote.response

data class BaseResponse<T>(
    val success: Boolean,
    val code: Int,
    val message: String,
    val result: T? = null,
)
