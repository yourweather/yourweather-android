package com.umc.yourweather.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.yourweather.data.ItemWrittenSun
import com.umc.yourweather.databinding.ActivityWrittenDetailListThunderBinding
import com.umc.yourweather.presentation.adapter.WrittenSunRVAdapter

class WrittenDetailListActivityThunder : AppCompatActivity() {
    private lateinit var binding: ActivityWrittenDetailListThunderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityWrittenDetailListThunderBinding.inflate(layoutInflater)
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
        dataList.add(ItemWrittenSun(6, 15, 7, 24))
        dataList.add(ItemWrittenSun(7, 13, 11, 54))
        dataList.add(ItemWrittenSun(7, 19, 16, 10))
        dataList.add(ItemWrittenSun(7, 25, 16, 10))
        dataList.add(ItemWrittenSun(7, 26, 20, 11))
        dataList.add(ItemWrittenSun(7, 20, 11, 1))

        return dataList
    }
}
