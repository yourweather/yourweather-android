package com.umc.yourweather.presentation.analysis

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.yourweather.data.ItemWritten
import com.umc.yourweather.databinding.ActivityWrittenDetailListRainWeeklyBinding
import com.umc.yourweather.presentation.adapter.WrittenRVAdapter

class WrittenDetailListActivityRainWeekly : AppCompatActivity() {
    private lateinit var binding: ActivityWrittenDetailListRainWeeklyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityWrittenDetailListRainWeeklyBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnAllwrittenLeftArrow.setOnClickListener {
            finish()
        }
        val dataList = fetchDataFromAPI()

        binding.recyclerViewUnwrittenDetail.layoutManager = LinearLayoutManager(this)
        val adapter = WrittenRVAdapter(dataList, this)
        binding.recyclerViewUnwrittenDetail.adapter = adapter
    }
    private fun fetchDataFromAPI(): List<ItemWritten> {
        val dataList = mutableListOf<ItemWritten>()
        dataList.add(ItemWritten(6, 21, "목", "오전", 8, 2))
        dataList.add(ItemWritten(6, 22, "월", "오전", 10, 51))
        dataList.add(ItemWritten(7, 20, "수", "오전", 12, 40))
        dataList.add(ItemWritten(7, 22, "일", "오후", 1, 43))

        return dataList
    }
}
