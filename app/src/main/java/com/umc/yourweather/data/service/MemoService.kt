package com.umc.yourweather.data.service

import com.umc.yourweather.data.remote.request.MemoRequest
import com.umc.yourweather.data.remote.request.MemoUpdateRequest
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.MemoDailyResponse
import com.umc.yourweather.data.remote.response.MemoResponse
import com.umc.yourweather.data.remote.response.MemoUpdateResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MemoService {

    // 메모 수정
    @PUT("/api/v1/memo/update/{memoId}")
    fun memoUpdate(
        @Path("memoId") memoId: Int,
        @Body request: MemoUpdateRequest,
    ): Call<BaseResponse<MemoUpdateResponse>>

    // 메모 작성
    @POST("/api/v1/memo/write")
    fun memoWrite(@Body request: MemoRequest): Call<BaseResponse<MemoResponse>>

    // 메모 삭제
    @DELETE("/api/v1/memo/delete/{memoId}")
    fun memoDelete(
        @Path("memoId") memoId: Int,
    ): Call<BaseResponse<Unit>>

    // 메모 하루치 반환
    @GET("/api/v1/memo/daily/{weatherId}")
    fun memoReturn(
        @Path("weatherId") weatherId: Int,
    ): Call<BaseResponse<BaseResponse<MemoDailyResponse>>>
}
