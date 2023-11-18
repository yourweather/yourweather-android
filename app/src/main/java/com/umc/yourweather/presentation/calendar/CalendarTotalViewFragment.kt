package com.umc.yourweather.presentation.calendar

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.umc.yourweather.R
import com.umc.yourweather.data.entity.CalendarDateInfo
import com.umc.yourweather.databinding.FragmentCalendarTotalViewBinding
import com.umc.yourweather.presentation.adapter.CalendarMonthAdapter
import com.umc.yourweather.presentation.adapter.CalendarSelectAdapter
import com.umc.yourweather.util.CalendarUtils.Companion.dpToPx
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class CalendarTotalViewFragment : Fragment() {
    lateinit var binding: FragmentCalendarTotalViewBinding
    lateinit var monthrAdapter: CalendarMonthAdapter
    lateinit var popupWindow: PopupWindow
    lateinit var calendarDateInfo: CalendarDateInfo
    var currentPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentCalendarTotalViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        monthrAdapter = CalendarMonthAdapter(requireActivity())

        binding.vp2Calendar.adapter = monthrAdapter
        binding.vp2Calendar.setCurrentItem(CalendarMonthAdapter.START_POSITION, false)
        setDateInfo()

        // 전으로 이동
        binding.btnCalendarBefore.setOnClickListener {
            moveDate(-1, true)
        }

        binding.btnCalendarNext.setOnClickListener {
            moveDate(1, true)
        }
        // 연월띄우기
        binding.vp2Calendar.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                Log.d("onPageSelected", "앞앞")
                setDateInfo()
            }
        })

        binding.llCalendarYear.setOnClickListener {
            showPopupWindow(it)
        }

        binding.btnCalendarYear.setOnClickListener {
            val anchorView = binding.llCalendarYear
            showPopupWindow(anchorView)
        }
    }

    private fun setDateInfo() {
        currentPosition = binding.vp2Calendar.currentItem
        val itemId = monthrAdapter.getItemId(currentPosition).toInt()

        calendarDateInfo = CalendarDateInfo(itemId / 100, itemId % 100)
        binding.tvCalendarMonth.text = calendarDateInfo.month.toString() + "월"
        binding.tvCalendarYear.text = calendarDateInfo.year.toString() + "년"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun showPopupWindow(anchorView: View) {
        val popupView =
            LayoutInflater.from(requireContext()).inflate(R.layout.popupwindow_calendar, null)
        val rcView = popupView.findViewById<RecyclerView>(R.id.rv_caledar_popup)
        var moveDates = addMoveDate()

        val adapter = CalendarSelectAdapter(requireContext(), moveDates, calendarDateInfo)

        rcView.adapter = adapter
        rcView.layoutManager = LinearLayoutManager(requireContext())

        val popupWidth = dpToPx(requireContext(), 190)
        val popupHeight = dpToPx(requireContext(), 462)

        popupWindow = PopupWindow(popupView, popupWidth, popupHeight, true)

        popupWindow.isOutsideTouchable = true

        val anchorLocation = IntArray(2)
        anchorView.getLocationOnScreen(anchorLocation)
        val anchorX = anchorLocation[0] + anchorView.width / 2
        val anchorY = anchorLocation[1] + anchorView.height

        // 팝업 윈도우를 anchorView의 가운데로 이동시킴
        val offsetX = -popupWidth / 2
        val offsetY = 0
        // val offsetY = -popupHeight / 2
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, anchorX + offsetX, anchorY + offsetY)

        // popupWindow.showAsDropDown(anchorView)
        binding.viewBackgroundView.visibility = View.VISIBLE

        popupWindow.setOnDismissListener {
            binding.viewBackgroundView.visibility = View.INVISIBLE
        }

        adapter.setOnItemClickListener(object : CalendarSelectAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val selectedDate = moveDates[position]
                val date1 = LocalDate.of(calendarDateInfo.year, calendarDateInfo.month, 1)
                val date2 = LocalDate.of(selectedDate.year, selectedDate.month, 1)

                // 개월수차이
                val monthsDifference = ChronoUnit.MONTHS.between(date1, date2)
                moveDate(monthsDifference.toInt(), false)
                Log.d("ListView Click", "$monthsDifference 개월")
                Log.d("ListView Click", "Selected Date: ${selectedDate.year}")
                popupWindow.dismiss()
            }
        })
    }

    fun moveDate(move: Int, smoothScroll: Boolean) {
        currentPosition = binding.vp2Calendar.currentItem
        binding.vp2Calendar.setCurrentItem(currentPosition + move, smoothScroll)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addMoveDate(): ArrayList<CalendarDateInfo> {
        var moveDates = ArrayList<CalendarDateInfo>()

        val todayDate = LocalDate.now()
        // CurrentDate : 현재 위치하고 있는곳
        var currentDate = LocalDate.of(calendarDateInfo.year, calendarDateInfo.month, calendarDateInfo.date)

        // 현재 기준 이번달~현재 위치하고 있는 달 전까지
        var tmpDate = todayDate
        while (!tmpDate.monthValue.equals(currentDate.monthValue)) {
            moveDates.add(CalendarDateInfo(tmpDate.year, tmpDate.monthValue, 1))
            tmpDate = tmpDate.minusMonths(1)
        }

        // 현재 위치하고 있는달 ~ 그로부터 10년간의 달
        for (i in 1..120) {
            moveDates.add(CalendarDateInfo(tmpDate.year, tmpDate.monthValue, 1))
            tmpDate = tmpDate.minusMonths(1)
        }

        return moveDates
    }
}
