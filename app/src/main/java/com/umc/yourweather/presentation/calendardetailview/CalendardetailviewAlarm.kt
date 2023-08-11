package com.umc.yourweather.presentation.calendardetailview

import android.os.Bundle
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.R
import java.text.SimpleDateFormat
import java.util.*

class CalendardetailviewAlarm : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendardetailview_alarm)

        val alertDialogBuilder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_time_picker, null)
        val timePicker = dialogView.findViewById<TimePicker>(R.id.timePicker)

        // 시간 선택 모드를 12시간 형식으로 설정
        timePicker.setIs24HourView(false)

        alertDialogBuilder.setView(dialogView)
            .setPositiveButton("OK") { dialog, _ ->
                val selectedHour: Int
                val selectedMinute: Int
                val amPm: String

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    selectedHour = timePicker.hour
                    selectedMinute = timePicker.minute
                } else {
                    selectedHour = timePicker.currentHour
                    selectedMinute = timePicker.currentMinute
                }

                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                calendar.set(Calendar.MINUTE, selectedMinute)

                val amPmFormat = SimpleDateFormat("a", Locale.getDefault())
                amPm = amPmFormat.format(calendar.time)

                val formattedTime = String.format("%02d:%02d %s", if (selectedHour % 12 == 0) 12 else selectedHour % 12, selectedMinute, amPm)

                // TODO: 선택한 시간을 처리하는 코드 작성

                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}
