package com.umc.yourweather.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.yourweather.data.ItemWrittenSun
import com.umc.yourweather.databinding.ActivityWrittenDetailListSunBinding
import com.umc.yourweather.presentation.adapter.WrittenSunRVAdapter

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
        val adapter = WrittenSunRVAdapter(dataList)
        binding.recyclerViewUnwrittenDetail.adapter = adapter
    }

    private fun fetchDataFromAPI(): List<ItemWrittenSun> {
        val dataList = mutableListOf<ItemWrittenSun>()
        dataList.add(ItemWrittenSun(6, 1, 10, 30))
        dataList.add(ItemWrittenSun(7, 1, 12, 55))
        dataList.add(ItemWrittenSun(7, 2, 18, 10))
        dataList.add(ItemWrittenSun(7, 3, 20, 10))
        dataList.add(ItemWrittenSun(7, 4, 12, 14))
        dataList.add(ItemWrittenSun(7, 5, 19, 20))

        return dataList
    }
}
