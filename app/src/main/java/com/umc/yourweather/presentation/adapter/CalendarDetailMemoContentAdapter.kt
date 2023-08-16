package com.umc.yourweather.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.yourweather.data.remote.response.MemoDailyResponse
import com.umc.yourweather.databinding.ItemCalendarDetailMemocontentBinding

class CalendarDetailMemoContentAdapter(private val memoContentList: List<MemoDailyResponse.MemoContentResponse>) :
    RecyclerView.Adapter<CalendarDetailMemoContentAdapter.CalendarDetailMemoContentViewHolder>() {

    override fun getItemCount(): Int = memoContentList.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CalendarDetailMemoContentViewHolder {
        val binding = ItemCalendarDetailMemocontentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarDetailMemoContentViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CalendarDetailMemoContentViewHolder,
        position: Int,
    ) {
        holder.binding.tvItemCalendarDetailMemocontentContent.text = memoContentList[position].content
        holder.binding.tvItemCalendarDetailMemocontentDate.text = memoContentList[position].creationDatetime
    }

    class CalendarDetailMemoContentViewHolder(var binding: ItemCalendarDetailMemocontentBinding) : RecyclerView.ViewHolder(binding.root)
}
