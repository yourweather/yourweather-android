package com.umc.yourweather.presentation.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.yourweather.data.ItemWritten
import com.umc.yourweather.databinding.ItemWrittenDetailSunBinding
import com.umc.yourweather.presentation.calendar.CalendarTotalViewFragment

class WrittenRVAdapter(private val dataList: List<ItemWritten>, private val context: Context) :
    RecyclerView.Adapter<WrittenRVAdapter.MyViewHolder>() {

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

    inner class MyViewHolder(val binding: ItemWrittenDetailSunBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ItemWritten) {
            if (data.minute < 10) {
                binding.tvStaticIconDetailSunny.text =
                    "${data.month}월 ${data.day}일 ${data.date}요일 ${data.mer} ${data.hour}:0${data.minute}"
            } else {
                binding.tvStaticIconDetailSunny.text =
                    "${data.month}월 ${data.day}일 ${data.date}요일 ${data.mer} ${data.hour}:${data.minute}"
            }

            binding.btnStaticsRightDetail1.setOnClickListener {
                val intent = Intent(context, CalendarTotalViewFragment::class.java)
                context.startActivity(intent)
            }
        }
    }
}
