package com.umc.yourweather.presentation.calendardetailview

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.umc.yourweather.R
import com.umc.yourweather.databinding.FragmentModifyBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ModifyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ModifyFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentModifyBinding

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
        binding = FragmentModifyBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // btn_home_weather_save 버튼 클릭 이벤트 처리
        binding.btnCalendardetailviewSave.setOnClickListener {
            val intent = Intent(requireContext(), CalendarDetailView3::class.java)
            startActivity(intent)
        }

        val year = requireActivity().intent.getStringExtra("year")
        val month = requireActivity().intent.getStringExtra("month")
        val date = requireActivity().intent.getStringExtra("date")
        val time = requireActivity().intent.getStringExtra("time")

        val textView: TextView = binding.tvCalendarDetailviewModify11
        textView.text = "${month}월 ${date}일 ${time}"

        // back 이미지 버튼 클릭 시 AlertDialog 표시
        binding.btnCalendarDetailviewBack.setOnClickListener {
            showLeaveAlertDialog()
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
        val cancelButton = dialogView.findViewById<Button>(R.id.noBtn)
        val confirmButton = dialogView.findViewById<Button>(R.id.yesBtn)

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
         * @return A new instance of fragment ModifyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ModifyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}