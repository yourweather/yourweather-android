package com.umc.yourweather.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.yourweather.data.ItemWritten
import com.umc.yourweather.databinding.ActivityWrittenDetailListRainBinding
import com.umc.yourweather.presentation.adapter.WrittenRVAdapter

class WrittenDetailListActivityRain : AppCompatActivity() {
    private lateinit var binding: ActivityWrittenDetailListRainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityWrittenDetailListRainBinding.inflate(layoutInflater)
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
        dataList.add(ItemWritten(6, 21, "월", "오전", 8, 2))
        dataList.add(ItemWritten(6, 22, "토", "오전", 10, 51))
        dataList.add(ItemWritten(7, 20, "일", "오후", 6, 40))
        dataList.add(ItemWritten(7, 22, "수", "오전", 11, 43))

        return dataList
    }
}
