package com.umc.yourweather.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.yourweather.data.remote.response.MemoDailyResponse
import com.umc.yourweather.databinding.ItemCalendarDetailMemolistBinding
import com.umc.yourweather.util.ResourceUtils.Companion.setWeatherIc

class CalendarDetailMemoListAdapter(private val memoList: List<MemoDailyResponse.MemoItemResponse>, private val context: Context) :
    RecyclerView.Adapter<CalendarDetailMemoListAdapter.CalendarDetailMemoListViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int, memoId: Int)
    }

    var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun getItemCount(): Int = memoList.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CalendarDetailMemoListViewHolder {
        val binding = ItemCalendarDetailMemolistBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return CalendarDetailMemoListViewHolder(binding, listener, memoList)
    }

    override fun onBindViewHolder(
        holder: CalendarDetailMemoListViewHolder,
        position: Int,
    ) {
        val weatherIc = setWeatherIc(context, memoList[position].status)
        holder.binding.ivItemCalendarDetailMemolistIc.setImageDrawable(weatherIc)
        holder.binding.tvItemCalendarDetailMemolistTemper.text =
            memoList[position].temperature.toString() + "°"
        holder.binding.tvItemCalendarDetailMemolistTime.text = memoList[position].creationDatetime
    }

    class CalendarDetailMemoListViewHolder(
        var binding: ItemCalendarDetailMemolistBinding,
        listener: OnItemClickListener?,
        memoList: List<MemoDailyResponse.MemoItemResponse>,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                listener?.onItemClick(it, bindingAdapterPosition, memoList[bindingAdapterPosition].memoId)
            }
        }
    }
}
