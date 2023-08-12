package com.umc.yourweather.data.service

import com.umc.yourweather.data.enums.Status
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.HomeResponse
import com.umc.yourweather.data.remote.response.MissedInputResponse
import com.umc.yourweather.data.remote.response.MonthResponse
import com.umc.yourweather.data.remote.response.WeatherResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.Month

interface WeatherService {
    // 날씨 미입력 조회
    @GET("/api/v1/weather/no-inputs")
    fun getMissedInput(): Call<BaseResponse<MissedInputResponse>>

    // 홈화면 데이터 조회
    @GET("/api/v1/weather/home")
    fun getHomeData(): Call<BaseResponse<HomeResponse>>

    // 월별 데이터 조회
    @GET("/api/v1/weather/monthly/{year}/{month}")
    fun getMonthData(
        @Query("year") year: Int,
        @Query("month") month: Int,
    ): Call<BaseResponse<MonthResponse>>

    // 월별 조회
    @DELETE("/api/v1/weather/{year}-{month}-{day}")
    fun deleteWeather(
        @Path("year") year: Int,
        @Path("month") month: Int,
        @Path("day") day: Int,
    ): Call<BaseResponse<WeatherResponse>>
}
