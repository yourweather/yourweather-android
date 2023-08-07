package com.umc.yourweather.remote.response

data class SpecificMemoResponse(
    val memoList: List<MemoReportResponse>,
    val proportion: Float
) {
    data class MemoReportResponse(
        val memoId: Long,
        val dateTime: String
    )
}