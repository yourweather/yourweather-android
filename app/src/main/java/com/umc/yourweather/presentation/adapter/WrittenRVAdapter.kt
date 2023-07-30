package com.umc.yourweather.presentation.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.yourweather.data.ItemWritten
import com.umc.yourweather.databinding.ItemWrittenDetailSunBinding
import com.umc.yourweather.presentation.CalendarView

class WrittenRVAdapter(private val dataList: List<ItemWritten>, private val context: Context) :
    RecyclerView.Adapter<WrittenRVAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemWrittenDetailSunBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
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
            binding.tvStaticIconDetailSunny.text = "${data.month}월 ${data.day}일 ${data.hour}:${data.minute}"

            // 버튼 클릭 이벤트 처리
            binding.btnStaticsRightDetail1.setOnClickListener {
                val intent = Intent(context, CalendarView::class.java)
                context.startActivity(intent)
            }
        }
    }
}

