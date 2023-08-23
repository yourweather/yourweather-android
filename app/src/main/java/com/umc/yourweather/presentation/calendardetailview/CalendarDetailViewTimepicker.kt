package com.umc.yourweather.presentation.calendardetailview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.yourweather.R
import com.umc.yourweather.databinding.FragmentCalendarDetailviewTimepickerBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalendarDetailViewTimepicker : Fragment() {

    private lateinit var binding: FragmentCalendarDetailviewTimepickerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentCalendarDetailviewTimepickerBinding.inflate(inflater, container, false)
        return binding.root
        return inflater.inflate(R.layout.fragment_calendar_detailview_timepicker, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val timePicker = binding.tpCalendardetailview

        binding.btnCalendardetailviewTimepickerSelect.setOnClickListener {
            // 선택한 시간 정보 저장
            val selectedHour = timePicker.hour
            val selectedMinute = timePicker.minute
            val displayHour: Int
            Log.d("타임피커값 확인", "$selectedHour $selectedMinute")

            // 타임피커에 보여주기 위한 값 오후 05 : 47 꼴
            val formattedTime = formatTime(selectedHour, selectedMinute)
            // 타임피커에 보여주기 위한 값 2023-08-19T17:48 꼴
            val localDateTime = formatLocalDateTime(selectedHour, selectedMinute)

            val timeText = formattedTime

            (activity as? CalendarPlusWeather)?.updateTimeText(timeText)
            (activity as? TimePickerListener)?.onTimeSelected(localDateTime)

            parentFragmentManager.popBackStack()
        }
    }
    interface TimePickerListener {
        fun onTimeSelected(localDateTime: String)
    }
    private fun formatLocalDateTime(selectedHour: Int, selectedMinute: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
        calendar.set(Calendar.MINUTE, selectedMinute)

        val sdf = SimpleDateFormat("'T'HH:mm", Locale.getDefault())
        return sdf.format(calendar.time)
    }

    private fun formatTime(selectedHour: Int, selectedMinute: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
        calendar.set(Calendar.MINUTE, selectedMinute)

        val sdf = SimpleDateFormat("a hh:mm", Locale.KOREA)
        return sdf.format(calendar.time)
    }
}
