package com.umc.yourweather.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.yourweather.data.ItemWritten
import com.umc.yourweather.databinding.ActivityWrittenDetailListSunBinding
import com.umc.yourweather.presentation.adapter.WrittenRVAdapter

class WrittenDetailListActivitySun : AppCompatActivity() {
    private lateinit var binding: ActivityWrittenDetailListSunBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityWrittenDetailListSunBinding.inflate(layoutInflater)
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
        dataList.add(ItemWritten(6, 1, "금","오전",10, 30))
        dataList.add(ItemWritten(7, 1, "목","오전",12, 55))
        dataList.add(ItemWritten(7, 2, "월","오후",6, 10))
        dataList.add(ItemWritten(7, 3, "수","오전",8, 10))
        dataList.add(ItemWritten(7, 4, "월","오후",12, 14))
        dataList.add(ItemWritten(7, 5, "토","오후",7, 20))

        return dataList
    }
}
