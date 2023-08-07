package com.umc.yourweather.data.remote.response

import com.google.gson.annotations.SerializedName

data class MissedInputResponse(
    @SerializedName("missed_input_localdates")
    val localDates: List<String>
)
