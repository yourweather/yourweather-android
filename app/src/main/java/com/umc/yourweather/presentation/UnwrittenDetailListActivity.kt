package com.umc.yourweather.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.yourweather.data.ItemUnwritten
import com.umc.yourweather.databinding.ActivityUnwrittenDetailListBinding
import com.umc.yourweather.presentation.adapter.UnwrittenRVAdapter

class UnwrittenDetailListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUnwrittenDetailListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUnwrittenDetailListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnUnwrittenLeftArrow.setOnClickListener {
            finish()
        }
        val dataList = fetchDataFromAPI()

        binding.recyclerViewUnwrittenDetail.layoutManager = LinearLayoutManager(this)
        val adapter = UnwrittenRVAdapter(dataList)
        binding.recyclerViewUnwrittenDetail.adapter = adapter
    }

    private fun fetchDataFromAPI(): List<ItemUnwritten> {
        val dataList = mutableListOf<ItemUnwritten>()
        dataList.add(ItemUnwritten(6, 23, "월"))
        dataList.add(ItemUnwritten(7, 15, "수"))
        dataList.add(ItemUnwritten(7, 20, "목"))
        dataList.add(ItemUnwritten(6, 23, "월"))
        dataList.add(ItemUnwritten(7, 15, "수"))
        dataList.add(ItemUnwritten(7, 20, "목"))
        dataList.add(ItemUnwritten(6, 23, "월"))
        dataList.add(ItemUnwritten(7, 15, "수"))
        dataList.add(ItemUnwritten(7, 20, "목"))
        dataList.add(ItemUnwritten(6, 23, "월"))
        dataList.add(ItemUnwritten(7, 15, "수"))
        dataList.add(ItemUnwritten(7, 20, "목"))
        dataList.add(ItemUnwritten(6, 23, "월"))
        dataList.add(ItemUnwritten(7, 15, "수"))
        dataList.add(ItemUnwritten(7, 20, "목"))

        return dataList
    }
}
