package com.umc.yourweather.presentation.analysis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.yourweather.R
import com.umc.yourweather.data.entity.ItemWritten
import com.umc.yourweather.databinding.FragmentWrittenDetailListSunWeeklyBinding
import com.umc.yourweather.presentation.adapter.WrittenRVAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WrittenDetailListFragmentSunWeekly : Fragment() {
    private var _binding: FragmentWrittenDetailListSunWeeklyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWrittenDetailListSunWeeklyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataList = fetchDataFromAPI()

        binding.recyclerViewDetailWeeklySun.layoutManager = LinearLayoutManager(requireContext())
        val adapter = WrittenRVAdapter(dataList, requireContext())
        binding.recyclerViewDetailWeeklySun.adapter = adapter

        binding.rlBtn1.setOnClickListener {
            navigateToAnalysisFragment()
        }

        // 뒤로가기 버튼을 누를 때 AnalysisFragment로 이동하도록 설정
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToAnalysisFragment()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        // 인자(bundle)로부터 ago 값을 가져오기
        val updateWeek = arguments?.getInt("updateWeek", 0) ?: 0
        // 정확한 주 숫자
        val getWeekText = getWeekText(updateWeek)
        binding.tvWrittenDetailListMonthSun.text = "이번 주(${getWeekText})의 맑음 통계"
    }
    private fun getWeekText(updateWeek: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.WEEK_OF_YEAR, -updateWeek)

        val dateFormat = SimpleDateFormat("M.d", Locale.getDefault())

        // Find the first day of the specified previous week (Monday)
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DAY_OF_WEEK, -1)
        }
        val previousWeekStart = dateFormat.format(calendar.time)

        // Find the last day of the specified previous week (Sunday)
        calendar.add(Calendar.DAY_OF_WEEK, 6)
        val previousWeekEnd = dateFormat.format(calendar.time)

        return "$previousWeekStart ~ $previousWeekEnd"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToAnalysisFragment() {
        val analysisFragment = AnalysisFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fl_content, analysisFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun fetchDataFromAPI(): List<ItemWritten> {
        val dataList = mutableListOf<ItemWritten>()
        dataList.add(ItemWritten(7, 31, "월", "오후", 6, 10))
        dataList.add(ItemWritten(8, 1, "화", "오후", 6, 10))


        return dataList
    }
}
