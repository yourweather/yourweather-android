package com.umc.yourweather.presentation.calendar // ktlint-disable filename

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

data class Info(var weather: String, var temper: Int, var date: LocalDate) {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun toString() =
        "add(Info(\"$weather\", $temper, ${date.year}, ${date.monthValue}, ${date.dayOfMonth}))"
}

@RequiresApi(Build.VERSION_CODES.O)
class testCalendarData {

    val weatherDatas = ArrayList<Info>()
    init {
        weatherDatas.add(Info("cloudy", 36, LocalDate.of(2023, 7, 1)))
        weatherDatas.add(Info("cloudy", 20, LocalDate.of(2023, 7, 2)))
        weatherDatas.add(Info("lightning", 39, LocalDate.of(2023, 7, 3)))
        weatherDatas.add(Info("rainy", 98, LocalDate.of(2023, 7, 4)))
        weatherDatas.add(Info("lightning", 37, LocalDate.of(2023, 7, 5)))
        weatherDatas.add(Info("sunny", 43, LocalDate.of(2023, 7, 6)))
        weatherDatas.add(Info("cloudy", 54, LocalDate.of(2023, 7, 7)))
        weatherDatas.add(Info("lightning", 23, LocalDate.of(2023, 7, 8)))
        weatherDatas.add(Info("cloudy", 75, LocalDate.of(2023, 7, 9)))
        weatherDatas.add(Info("sunny", 64, LocalDate.of(2023, 7, 10)))
        weatherDatas.add(Info("lightning", 82, LocalDate.of(2023, 7, 11)))
        weatherDatas.add(Info("sunny", 95, LocalDate.of(2023, 7, 12)))
        weatherDatas.add(Info("lightning", 5, LocalDate.of(2023, 7, 13)))
        weatherDatas.add(Info("lightning", 21, LocalDate.of(2023, 7, 14)))
        weatherDatas.add(Info("sunny", 93, LocalDate.of(2023, 7, 15)))
        weatherDatas.add(Info("cloudy", 99, LocalDate.of(2023, 7, 16)))
        weatherDatas.add(Info("lightning", 59, LocalDate.of(2023, 7, 17)))
        weatherDatas.add(Info("cloudy", 56, LocalDate.of(2023, 7, 18)))
        weatherDatas.add(Info("rainy", 13, LocalDate.of(2023, 7, 19)))
        weatherDatas.add(Info("cloudy", 41, LocalDate.of(2023, 7, 20)))
        weatherDatas.add(Info("rainy", 52, LocalDate.of(2023, 7, 21)))
        weatherDatas.add(Info("rainy", 95, LocalDate.of(2023, 7, 22)))
        weatherDatas.add(Info("cloudy", 75, LocalDate.of(2023, 7, 23)))
        weatherDatas.add(Info("cloudy", 32, LocalDate.of(2023, 7, 24)))
        weatherDatas.add(Info("lightning", 69, LocalDate.of(2023, 7, 25)))
        weatherDatas.add(Info("sunny", 66, LocalDate.of(2023, 7, 26)))
        weatherDatas.add(Info("cloudy", 36, LocalDate.of(2023, 7, 27)))
        weatherDatas.add(Info("cloudy", 9, LocalDate.of(2023, 7, 28)))
        weatherDatas.add(Info("sunny", 21, LocalDate.of(2023, 7, 29)))
        weatherDatas.add(Info("sunny", 84, LocalDate.of(2023, 7, 30)))
        weatherDatas.add(Info("lightning", 29, LocalDate.of(2023, 7, 31)))
        weatherDatas.add(Info("cloudy", 100, LocalDate.of(2023, 8, 1)))
        weatherDatas.add(Info("rainy", 17, LocalDate.of(2023, 8, 2)))
        weatherDatas.add(Info("sunny", 65, LocalDate.of(2023, 8, 3)))
        weatherDatas.add(Info("sunny", 100, LocalDate.of(2023, 8, 4)))
    }
}
