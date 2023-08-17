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
class WrittenRVAdapter(private val dataList: List<ItemWritten>, private val context: Context, private val memoIds: List<Int>) :
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
        val memoId = memoIds[position] // 가져온 메모 아이디 리스트에서 현재 아이템 위치에 해당하는 아이디를 가져옴
        holder.bind(data, memoId)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class MyViewHolder(val binding: ItemWrittenDetailSunBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ItemWritten, memoId: Int) {
            binding.tvStaticIconDetailSunny.text = "${data.formattedDateTime}"

            binding.linearLayout3.setOnClickListener {
                val intent = Intent(context, CalendarDetailviewModify1::class.java)
                intent.putExtra("dateTime", data.dateTime) // 전달할 데이터 추가
                intent.putExtra("memoIdW", memoId) // 수정된 코드: 메모 아이디 대신 memoId 전달
                context.startActivity(intent)

                // 로그로 확인
                Log.d("WrittenRVAdapter", "Clicked Item - DateTime: ${data.dateTime}, MemoId: $memoId")
            }
        }
    }
}

