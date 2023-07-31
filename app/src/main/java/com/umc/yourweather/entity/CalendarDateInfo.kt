package com.umc.yourweather.entity

data class CalendarDateInfo(val year: Int, val month: Int, val date: Int = 1) {
    override fun toString(): String = "${year}년 ${month}월"
}
