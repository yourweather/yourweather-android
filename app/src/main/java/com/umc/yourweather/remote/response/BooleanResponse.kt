package com.umc.yourweather.remote.response

import com.google.gson.annotations.SerializedName
data class BooleanResponse(
    val success: Boolean,
    val code: Int,
    val message: String,
    val result: Boolean
)
