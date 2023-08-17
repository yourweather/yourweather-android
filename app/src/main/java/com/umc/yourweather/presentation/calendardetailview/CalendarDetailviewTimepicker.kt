package com.umc.yourweather.presentation.calendardetailview

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
 * Use the [CalendarDetailviewTimepicker.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalendarDetailviewTimepicker : Fragment() {
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCalendarDetailviewTimepickerBinding.inflate(inflater, container, false)
        return binding.root
        return inflater.inflate(R.layout.fragment_calendar_detailview_timepicker, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val timePicker=binding.tpCalendardetailview
       
        binding.btnCalendardetailviewTimepickerSelect.setOnClickListener {
            // 오전 / 오후를 확인하기 위한 if 문
            val selectedHour = timePicker.hour
            val selectedMinute = timePicker.minute
            val displayHour: Int


            val formattedTime = formatTime(selectedHour, selectedMinute)
            val timeText = formattedTime


            (activity as? CalendarDetailviewModify2)?.updateTimeText(timeText)

            parentFragmentManager.popBackStack()
        }

    }
    private fun formatTime(selectedHour: Int, selectedMinute: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
        calendar.set(Calendar.MINUTE, selectedMinute)

        val sdf = SimpleDateFormat("a hh : mm", Locale.KOREA)
        return sdf.format(calendar.time)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CalendarDetailviewTimepicker.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CalendarDetailviewTimepicker().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}