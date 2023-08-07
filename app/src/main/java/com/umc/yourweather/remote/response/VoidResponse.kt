package com.umc.yourweather.remote.response

data class VoidResponse(
    val success: Boolean,
    val code: Int,
    val message: String,
    val result: Any?
)
