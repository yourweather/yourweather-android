package com.umc.yourweather.data.remote.response

import com.google.gson.annotations.SerializedName

data class MemoReportResponse(
    @SerializedName("memoId")
    val memoId: Long,
    @SerializedName("dateTime")
    val dateTime: String
)
