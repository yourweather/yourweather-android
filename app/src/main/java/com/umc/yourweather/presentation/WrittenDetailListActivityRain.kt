package com.umc.yourweather.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.yourweather.data.ItemWrittenSun
import com.umc.yourweather.databinding.ActivityWrittenDetailListRainBinding
import com.umc.yourweather.presentation.adapter.WrittenSunRVAdapter

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
        val adapter = WrittenSunRVAdapter(dataList)
        binding.recyclerViewUnwrittenDetail.adapter = adapter
    }

    private fun fetchDataFromAPI(): List<ItemWrittenSun> {
        val dataList = mutableListOf<ItemWrittenSun>()
        dataList.add(ItemWrittenSun(6, 21, 8, 2))
        dataList.add(ItemWrittenSun(6, 22, 10, 51))
        dataList.add(ItemWrittenSun(7, 20, 18, 40))
        dataList.add(ItemWrittenSun(7, 22, 11, 43))

        return dataList
    }
}
