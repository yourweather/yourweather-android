package com.umc.yourweather.data.remote.response

data class GetSpecificMemoResponse(
    val success: Boolean,
    val code: Int,
    val message: String,
    val result: com.umc.yourweather.data.remote.response.GetSpecificMemoResponse.SpecificMemoResponse
) {
    data class SpecificMemoResponse(
        val memoList: List<com.umc.yourweather.data.remote.response.GetSpecificMemoResponse.SpecificMemoResponse.MemoReportResponse>,
        val proportion: Float
    ) {
        data class MemoReportResponse(
            val memoId: Long,
            val dateTime: String
        )
    }
}

