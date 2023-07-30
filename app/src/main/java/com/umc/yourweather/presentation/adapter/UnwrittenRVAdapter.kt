package com.umc.yourweather.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.yourweather.data.ItemUnwritten
import com.umc.yourweather.data.ItemWritten
import com.umc.yourweather.databinding.ItemUnwrittenDetailBinding
import com.umc.yourweather.databinding.ItemWrittenDetailSunBinding

class UnwrittenRVAdapter(private val dataList: List<ItemUnwritten>) :
    RecyclerView.Adapter<UnwrittenRVAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUnwrittenDetailBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = dataList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class MyViewHolder(private val binding: ItemUnwrittenDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ItemUnwritten) {
            binding.tvUnwrittenDetail.text = "${data.month}월 ${data.day}일 ${data.date}요일"
        }
    }
}
