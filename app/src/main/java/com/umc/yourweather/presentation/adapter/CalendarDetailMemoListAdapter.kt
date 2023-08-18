package com.umc.yourweather.presentation.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.umc.yourweather.data.remote.response.MemoDailyResponse
import com.umc.yourweather.databinding.ItemCalendarDetailMemolistBinding
import com.umc.yourweather.presentation.calendardetailview.CalendarDetailviewModify1
import com.umc.yourweather.util.CalendarUtils.Companion.dpToPx
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

//        // 그래프그리기....
//        // 원 위치부터 구하기..
//        var totalGraphHeight = dpToPx(context, 90)
//        var temper = memoList[position].temperature
//        var circleHeight = totalGraphHeight / 100 * temper
//        // 원 높이는 전체 / 100 * 온도 수치(온도 범위가 0~100이므로.....)
//        // 그래프 아이콘의 아래 margin
//        var layoutParams = holder.binding.ivCalendarDetailGraph.layoutParams as ConstraintLayout.LayoutParams
//        layoutParams.bottomMargin = circleHeight
//        holder.binding.ivCalendarDetailGraph.layoutParams = layoutParams

        // 근데 사이즈 1이면(데이터 하나면 선은 필요가 없다..)
        // 0번째면 두번째 선만 필요할것같다
        // 마지막 순이면 첫번째 선만 필요함
//        if (memoList.size != 1) {
//            if (position == 0) {
//
//
//            } else if (position == memoList.size - 1) {
//
//
//            } else {
//
//
//            }
//        }

        // 첫번째 선

        // 두번째 선

        // holder.binding.ivCalendarDetailGraph.setImageResource(R)

//        // 클릭 시 메모 아이디를 가지고 modify1으로 이동
//        holder.binding.root.setOnClickListener {
//            val intent = Intent(context, CalendarDetailviewModify1::class.java)
//            val detailId = memoList[position].memoId
//            intent.putExtra("memoId", detailId)
//            context.startActivity(intent)
//            Log.d("상세보기 날씨 메모 아이디", "$detailId")
//        }
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
