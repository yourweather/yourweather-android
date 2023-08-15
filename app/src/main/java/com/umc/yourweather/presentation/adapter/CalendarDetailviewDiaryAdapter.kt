package com.umc.yourweather.presentation.adapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.umc.yourweather.R
import com.umc.yourweather.data.remote.response.MemoDailyResponse

class CalendarDetailviewDiaryAdapter(private val memoContentList: List<MemoDailyResponse.MemoContentResponse>):
    RecyclerView.Adapter<CalendarDetailviewDiaryAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        public var itemdetail: TextView = itemView.findViewById(R.id.tv_calendar_detailview_diary)
    }

    // 1. Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CalendarDetailviewDiaryAdapter.MyViewHolder {
        // create a new view
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_calendar_detailview, parent, false)

        return MyViewHolder(cardView)
    }

    // 2. Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val memoContent = memoContentList[position] // Get the MemoContentResponse at the current position
        holder.itemdetail.text = memoContent.status
    }

    // 3. Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return 5
    }
}