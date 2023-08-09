package com.umc.yourweather.presentation.calendardetailview

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatImageButton
import com.umc.yourweather.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.umc.yourweather.presentation.adapter.CalendarDetailviewDiaryAdapter


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ScrollviewFragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScrollviewFragment2 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button = view.findViewById<AppCompatImageButton>(R.id.btn_calendardetailview2_plus)

        button.setOnClickListener {
            val intent = Intent(activity, CalendarDetailviewModify1::class.java)
            startActivity(intent)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_scrollview2, container, false)

        // 프래그먼트에서의 findViewById 대신 데이터 바인딩을 사용하거나 rootView를 통해 뷰 요소를 찾습니다.
        val textView2: MaterialTextView = rootView.findViewById(R.id.tv_calendar_detailview2_1)


        val year = arguments?.getString("year")
        val month = arguments?.getString("month")
        val date = arguments?.getString("date")

        textView2.text = "${month}월 ${date}일의 일기"

        // 1
        val viewManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true)
        // 2
        val viewAdapter = CalendarDetailviewDiaryAdapter()

        // 3
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.recyclerview_calendar_detailview2).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        return rootView

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Scrollview2.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ScrollviewFragment2().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}