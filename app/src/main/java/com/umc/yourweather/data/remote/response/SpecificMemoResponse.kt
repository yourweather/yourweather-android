package com.umc.yourweather.data.remote.response

import com.google.gson.annotations.SerializedName

data class SpecificMemoResponse(
    @SerializedName("memoList")
    val memoList: List<MemoReportResponse>,
    @SerializedName("proportion")
    val proportion: Float,
) {
    data class MemoReportResponse(
        @SerializedName("memoId")
        val memoId: Long,
        @SerializedName("dateTime")
        val dateTime: String,
    )
}
