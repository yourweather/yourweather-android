package com.umc.yourweather.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.yourweather.databinding.TextviewItemListviewCalendarBinding
import com.umc.yourweather.entity.CalendarDateInfo

class CalendarSelectAdapter(
    private val context: Context,
    private val moveDates: ArrayList<CalendarDateInfo>,
    private val calendarDateInfo: CalendarDateInfo,
) : RecyclerView.Adapter<CalendarSelectAdapter.CalendarSelectorViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    lateinit var listener: OnItemClickListener
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarSelectorViewHolder {
        val itemView = TextviewItemListviewCalendarBinding.inflate(LayoutInflater.from(context), parent, false)

        return CalendarSelectorViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int {
        return moveDates.size
    }

    class CalendarSelectorViewHolder(val binding: TextviewItemListviewCalendarBinding, listener: OnItemClickListener) : RecyclerView.ViewHolder(binding.root) {
        // val textView: TextView = itemView.findViewById(R.id.tv_calendar_item)
        init {
            binding.root.setOnClickListener {
                listener.onItemClick(it!!, adapterPosition)
            }
        }
    }

    override fun onBindViewHolder(holder: CalendarSelectorViewHolder, position: Int) {
        val dateInfo = moveDates[position]
        holder.binding.tvCalendarListview.text = dateInfo.toString()
    }
}
