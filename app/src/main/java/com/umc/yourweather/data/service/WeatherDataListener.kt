package com.umc.yourweather.data.service

interface WeatherDataListener {
    fun onWeatherDataReceived(ago: Int, sunnyPercent: Int, cloudyPercent: Int, rainyPercent: Int, lightningPercent: Int)
}
