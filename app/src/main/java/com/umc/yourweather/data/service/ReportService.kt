package com.umc.yourweather.data.service

import com.umc.yourweather.data.remote.request.MemoRequest
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.MissedInputResponse
import com.umc.yourweather.data.remote.response.SpecificMemoResponse
import com.umc.yourweather.data.remote.response.StatisticResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ReportService {

    // 주간 통계 요청
    @GET("api/v1/report/weekly-statistic")
    fun weeklyStatistic(@Query("ago") ago: Int): Call<BaseResponse<StatisticResponse>>

    // 주간 데이터 비교
    @GET("api/v1/report/weekly-comparison")
    fun weeklyComparison(@Query("ago") ago: Int): Call<BaseResponse<StatisticResponse>>

    // 월간 통계 요청
    @GET("api/v1/report/monthly-statistic")
    fun monthlyStatistic(@Query("ago") ago: Int): Call<BaseResponse<StatisticResponse>>

    // 월간 데이터 비교
    @GET("api/v1/report/monthly-comparison")
    fun monthlyComparison(@Query("ago") ago: Int): Call<BaseResponse<StatisticResponse>>

    // 월 중 특정 날씨 리스트 요청
    @GET("api/v1/report/list")
    fun getMonthlyReport(
        @Query("month") month: Int,
        @Query("weather") weather: MemoRequest.Status,
    ): Call<BaseResponse<SpecificMemoResponse>>

    // 미입력 내역 조회
    @GET("api/v1/weather/no-inputs")
    fun noInput(): Call<BaseResponse<MissedInputResponse>>
}
