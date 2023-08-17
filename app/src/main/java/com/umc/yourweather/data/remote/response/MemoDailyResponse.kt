package com.umc.yourweather.data.remote.response

import com.google.gson.annotations.SerializedName

data class MemoDailyResponse(
    @SerializedName("memoList")
    val memoList: List<MemoItemResponse>,
    @SerializedName("memoContentList")
    val memoContentList: List<MemoContentResponse>,
) {
    data class MemoItemResponse(
        @SerializedName("memoId")
        val memoId: Int,
        @SerializedName("creationDatetime")
        var creationDatetime: String,
        @SerializedName("status")
        val status: com.umc.yourweather.data.enums.Status,
        @SerializedName("temperature")
        val temperature: Int,
    )

    data class MemoContentResponse(
        @SerializedName("memoId")
        val memoId: Int,
        @SerializedName("creationDatetime")
        var creationDatetime: String,
        @SerializedName("content")
        val content: String,
    )
}
