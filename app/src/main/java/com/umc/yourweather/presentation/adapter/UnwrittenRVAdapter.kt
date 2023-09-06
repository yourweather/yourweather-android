package com.umc.yourweather.presentation.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.yourweather.data.entity.ItemUnwritten
import com.umc.yourweather.databinding.ItemUnwrittenDetailBinding
import com.umc.yourweather.presentation.calendardetailview.CalendarPlusWeatherActivity

class UnwrittenRVAdapter(private val dataList: List<ItemUnwritten>, private val localDates: List<String>, private val context: Context) :
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

    @Suppress("DEPRECATION")
    inner class MyViewHolder(val binding: ItemUnwrittenDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ItemUnwritten) {
            binding.tvUnwrittenDetail.text = data.formattedDate
            val position = adapterPosition
            val localDate = localDates[position] // 해당 아이템의 localDate 가져오기

            binding.linearLayout3.setOnClickListener {
                val dIntent = Intent(context, CalendarPlusWeatherActivity::class.java)
                dIntent.putExtra("unWrittenDate", localDate)
                context.startActivity(dIntent)
                Log.d("localDate","$localDate")
            }
        }
    }
}
