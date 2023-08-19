package com.umc.yourweather.presentation.calendardetailview

import android.content.Intent
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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CalendarDetailViewTimepicker.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalendarDetailViewTimepicker : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentCalendarDetailviewTimepickerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
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

            parentFragmentManager.popBackStack()

            // 사용자가 선택한 값 CalendarPlusWeather 창으로 넘겨주기
            val intent = Intent(requireContext(), CalendarPlusWeather::class.java)
            intent.putExtra("selectedTime", localDateTime)
            Log.d("타임피커값 확인 selectedTime", "$localDateTime")
        }
    }

    private fun formatLocalDateTime(selectedHour: Int, selectedMinute: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
        calendar.set(Calendar.MINUTE, selectedMinute)

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
        return sdf.format(calendar.time)
    }

    private fun formatTime(selectedHour: Int, selectedMinute: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
        calendar.set(Calendar.MINUTE, selectedMinute)

        val sdf = SimpleDateFormat("a hh : mm", Locale.KOREA)
        return sdf.format(calendar.time)
    }
}
