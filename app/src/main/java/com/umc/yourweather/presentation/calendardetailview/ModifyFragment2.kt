package com.umc.yourweather.presentation.calendardetailview

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.umc.yourweather.R
import com.umc.yourweather.databinding.FragmentModify2Binding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ModifyFragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class ModifyFragment2 : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var editText: EditText
    private lateinit var button: Button
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentModify2Binding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentModify2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 프래그먼트 레이아웃의 버튼 참조
        button = view.findViewById(R.id.btn_calendardetailview_save)

        // 액티비티 레이아웃의 요소 참조
        editText = requireActivity().findViewById(R.id.editText)
        button = view.findViewById(R.id.btn_calendardetailview_save)


        // btn_home_weather_save 버튼 클릭 이벤트 처리
        binding.btnCalendardetailviewSave.setOnClickListener {
            val intent = Intent(requireContext(), CalendarDetailView3::class.java)
            startActivity(intent)
        }

        // EditText의 텍스트 변화를 감지하는 TextWatcher 설정
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateButtonState()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // 버튼 클릭 이벤트 설정
        button.setOnClickListener {
            // 버튼 클릭 시 EditText 내용 변경

        }

        // 초기 버튼 상태 설정
        updateButtonState()

        val dateTimeString = arguments?.getString("dateTime")
        if (dateTimeString != null) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            val date = dateFormat.parse(dateTimeString)
            val calendar = Calendar.getInstance()
            calendar.time = date

            val month = calendar.get(Calendar.MONTH) + 1
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val amPm = if (hour >= 12) "오후" else "오전"
            val displayHour = if (hour > 12) hour - 12 else hour

            val textView = view.findViewById<TextView>(R.id.tv_calendar_detailview_modify1_time)
            textView.text = "${month}월 ${day}일의 기록"
        }
    }


    private fun updateButtonState() {
        val text = editText.text.toString()

        // EditText 내용에 따라 버튼 상태 변경
        if (text.isNotEmpty()) {
            button.text = "저장"
            button.setTextColor(resources.getColor(R.color.sorange))
        } else {
            button.text = "저장"
            button.setTextColor(resources.getColor(R.color.gray))
        }
    }

    // AlertDialog를 표시하는 함수
    private fun showLeaveAlertDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.alertdialog_calendar_detailview_modify, null)
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)

        val alertDialog = alertDialogBuilder.create()

        // AlertDialog 내부 뷰 요소 설정
        val cancelButton = dialogView.findViewById<Button>(R.id.btn_alertdailog_calendar_detailview_modify_no)
        val confirmButton = dialogView.findViewById<Button>(R.id.btn_alertdailog_calendar_detailview_modify_yes)

        // Cancel 버튼 클릭 시 AlertDialog 닫기
        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }

        // Confirm 버튼 클릭 시 액티비티를 종료하고 AlertDialog 닫기
        confirmButton.setOnClickListener {
            val intent = Intent(requireContext(), CalendarDetailView3::class.java)
            alertDialog.dismiss()
            requireActivity().finish()
        }

        alertDialog.show()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ModifyFragment2.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ModifyFragment2().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}