package com.umc.yourweather.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.yourweather.data.ItemWrittenSun
import com.umc.yourweather.databinding.ItemWrittenDetailSunBinding

class WrittenSunRVAdapter(private val dataList: List<ItemWrittenSun>) :
    RecyclerView.Adapter<WrittenSunRVAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemWrittenDetailSunBinding.inflate(
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

    class MyViewHolder(private val binding: ItemWrittenDetailSunBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ItemWrittenSun) {
            binding.tvStaticIconDetailSunny.text = "${data.month}월 ${data.day}일 ${data.hour}:${data.minute}"
        }
    }
}
