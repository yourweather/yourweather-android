package com.umc.yourweather.presentation.analysis

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.umc.yourweather.R
import com.umc.yourweather.data.ItemWritten
import com.umc.yourweather.databinding.FragmentWrittenDetailListSunBinding
import com.umc.yourweather.presentation.adapter.WrittenRVAdapter

class WrittenDetailListFragmentSun : Fragment() {
    private var _binding: FragmentWrittenDetailListSunBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWrittenDetailListSunBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataList = fetchDataFromAPI()

        binding.recyclerViewDetailSun.layoutManager = LinearLayoutManager(requireContext())
        val adapter = WrittenRVAdapter(dataList, requireContext())
        binding.recyclerViewDetailSun.adapter = adapter

        binding.btnDetailLeftArrow.setOnClickListener {
            navigateToAnalysisFragment()
        }

        // 뒤로가기 버튼을 누를 때 AnalysisFragment로 이동하도록 설정
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToAnalysisFragment()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
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
        dataList.add(ItemWritten(6, 23, "월", "오전", 10, 35))
        dataList.add(ItemWritten(7, 15, "화", "오후", 2, 55))
        dataList.add(ItemWritten(7, 20, "수", "오후", 6, 10))

        return dataList
    }
}
