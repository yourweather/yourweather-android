package com.umc.yourweather.presentation.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.yourweather.data.entity.ItemWritten
import com.umc.yourweather.databinding.ItemWrittenDetailSunBinding
import com.umc.yourweather.presentation.calendardetailview.CalendarDetailviewModify1

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
            binding.tvStaticIconDetailSunny.text = "${data.formattedDateTime}"

            binding.linearLayout3.setOnClickListener {
                val intent = Intent(context, CalendarDetailviewModify1::class.java)
                intent.putExtra("dateTime", data.dateTime) // 전달할 데이터 추가
                intent.putExtra("memoId", data.memoId) // 전달할 메모 아이디 추가
                context.startActivity(intent)

                // 로그로 확인
                Log.d("WrittenRVAdapter", "Clicked Item - DateTime: ${data.dateTime}, MemoId: ${data.memoId}")
            }
        }
    }
}
