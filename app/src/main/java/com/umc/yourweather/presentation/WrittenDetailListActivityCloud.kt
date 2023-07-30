package com.umc.yourweather.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.yourweather.data.ItemWritten
import com.umc.yourweather.databinding.ActivityWrittenDetailListCloudBinding
import com.umc.yourweather.presentation.adapter.WrittenRVAdapter

class WrittenDetailListActivityCloud : AppCompatActivity() {
    private lateinit var binding: ActivityWrittenDetailListCloudBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityWrittenDetailListCloudBinding.inflate(layoutInflater)
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
        dataList.add(ItemWritten(6, 23, "월","오전",10, 35))
        dataList.add(ItemWritten(7, 15, "화","오후",2, 55))
        dataList.add(ItemWritten(7, 20, "수","오후",6, 10))

        return dataList
    }
}
