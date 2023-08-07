package com.umc.yourweather.data.remote.response

data class SpecificMemoResponse(
    val memoList: List<com.umc.yourweather.data.remote.response.SpecificMemoResponse.MemoReportResponse>,
    val proportion: Float,
) {
    data class MemoReportResponse(
        val memoId: Long,
        val dateTime: String,
    )
}
