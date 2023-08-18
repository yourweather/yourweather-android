package com.umc.yourweather.presentation.calendardetailview

interface TimePickerCallback {
    fun onTimeSelected(isAM: Boolean, hour: Int, minute: Int)

}