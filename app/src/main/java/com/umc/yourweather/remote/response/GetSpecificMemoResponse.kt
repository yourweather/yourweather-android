package com.umc.yourweather.remote.response

data class GetSpecificMemoResponse(
    val success: Boolean,
    val code: Int,
    val message: String,
    val result: SpecificMemoResponse
) {
    data class SpecificMemoResponse(
        val memoList: List<MemoReportResponse>,
        val proportion: Float
    ) {
        data class MemoReportResponse(
            val memoId: Long,
            val dateTime: String
        )
    }
}

