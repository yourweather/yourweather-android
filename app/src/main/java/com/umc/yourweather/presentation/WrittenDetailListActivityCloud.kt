package com.umc.yourweather.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.yourweather.data.ItemWrittenSun
import com.umc.yourweather.databinding.ActivityWrittenDetailListCloudBinding
import com.umc.yourweather.presentation.adapter.WrittenSunRVAdapter

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
        val adapter = WrittenSunRVAdapter(dataList)
        binding.recyclerViewUnwrittenDetail.adapter = adapter
    }
    private fun fetchDataFromAPI(): List<ItemWrittenSun> {
        val dataList = mutableListOf<ItemWrittenSun>()
        dataList.add(ItemWrittenSun(6, 23, 10, 35))
        dataList.add(ItemWrittenSun(7, 15, 14, 55))
        dataList.add(ItemWrittenSun(7, 20, 18, 10))

        return dataList
    }
}
