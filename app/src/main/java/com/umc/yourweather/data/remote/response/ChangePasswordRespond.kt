package com.umc.yourweather.data.remote.response

import com.google.gson.annotations.SerializedName

data class ChangePasswordRespond(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("occurredByDB")
    val occurredByDB: Boolean,
    @SerializedName("occurredByPassword")
    val occurredByPassword: Boolean
)
