package com.umc.yourweather.remote.response

data class BooleanResponse(
    val success: Boolean,
    val code: Int,
    val message: String,
    val result: Boolean
)
