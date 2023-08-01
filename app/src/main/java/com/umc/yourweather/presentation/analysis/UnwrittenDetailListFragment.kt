package com.umc.yourweather.presentation.analysis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.yourweather.data.ItemUnwritten
import com.umc.yourweather.databinding.FragmentUnwrittenDetailListBinding
import com.umc.yourweather.presentation.adapter.UnwrittenRVAdapter

class UnwrittenDetailListFragment : Fragment() {
    private var _binding: FragmentUnwrittenDetailListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentUnwrittenDetailListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnUnwrittenLeftArrow.setOnClickListener {
            requireActivity().finish()
        }

        val dataList = fetchDataFromAPI()

        binding.recyclerViewUnwrittenDetail.layoutManager = LinearLayoutManager(requireContext())
        val adapter = UnwrittenRVAdapter(dataList, requireContext())
        binding.recyclerViewUnwrittenDetail.adapter = adapter
    }

    private fun fetchDataFromAPI(): List<ItemUnwritten> {
        val dataList = mutableListOf<ItemUnwritten>()
        dataList.add(ItemUnwritten(6, 23, "월"))
        dataList.add(ItemUnwritten(7, 15, "수"))
        dataList.add(ItemUnwritten(7, 20, "목"))
        // Add more items as needed

        return dataList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
