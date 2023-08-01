package com.umc.yourweather.presentation.analysis

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.yourweather.data.ItemWritten
import com.umc.yourweather.databinding.ActivityWrittenDetailListThunderWeeklyBinding
import com.umc.yourweather.presentation.adapter.WrittenRVAdapter

class WrittenDetailListActivityThunderWeekly : AppCompatActivity() {
    private lateinit var binding: ActivityWrittenDetailListThunderWeeklyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityWrittenDetailListThunderWeeklyBinding.inflate(layoutInflater)
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
        dataList.add(ItemWritten(6, 15, "일", "오전", 7, 24))
        dataList.add(ItemWritten(7, 13, "화", "오전", 11, 54))
        dataList.add(ItemWritten(7, 19, "월", "오후", 4, 10))
        dataList.add(ItemWritten(7, 25, "수", "오후", 4, 10))
        dataList.add(ItemWritten(7, 26, "목", "오후", 8, 11))
        dataList.add(ItemWritten(7, 28, "토", "오전", 11, 1))

        return dataList
    }
}
