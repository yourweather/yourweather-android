package com.umc.yourweather.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.yourweather.data.ItemWritten
import com.umc.yourweather.databinding.ActivityWrittenDetailListSunWeeklyBinding
import com.umc.yourweather.presentation.adapter.WrittenRVAdapter

class WrittenDetailListActivitySunWeekly : AppCompatActivity() {
    private lateinit var binding: ActivityWrittenDetailListSunWeeklyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityWrittenDetailListSunWeeklyBinding.inflate(layoutInflater)
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
        dataList.add(ItemWritten(6, 1,"토", "오전",10, 30))
        dataList.add(ItemWritten(7, 1,"일","오후",12, 55))
        dataList.add(ItemWritten(7, 2,"월", "오후",6, 10))
        dataList.add(ItemWritten(7, 3,"화","오후",8, 10))
        dataList.add(ItemWritten(7, 4,"수", "오후",12, 14))
        dataList.add(ItemWritten(7, 5,"목","오전",7, 20))

        return dataList
    }
}
