package com.umc.yourweather.presentation.calendardetailview

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.material.textview.MaterialTextView
import com.umc.yourweather.R
import com.umc.yourweather.data.enums.Status
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.MemoDailyResponse
import com.umc.yourweather.data.service.MemoService
import com.umc.yourweather.databinding.FragmentHorizontalScroll2Binding
import com.umc.yourweather.di.RetrofitImpl
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HorizontalScrollFragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class HorizontalScrollFragment2 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentHorizontalScroll2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_horizontal_scroll2, container, false)
        val myTextView: MaterialTextView = rootView.findViewById(R.id.tv_horizontal_scroll2_time)

        myTextView.paintFlags = myTextView.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        // HorizontalScrollFragment2 내부의 클릭 이벤트 핸들러
        fun onDateClick(date: LocalDate, weatherId: Int?, dateTime: String) {
            val bundle = Bundle().apply {
                putString("dateTime", dateTime)
            }

            val modifyFragment = ModifyFragment()
            modifyFragment.arguments = bundle

            // Fragment 전환 코드 (생략)
        }

        myTextView.setOnClickListener {
            // Handle click event for myTextView1
            val intent = Intent(requireContext(), CalendarDetailviewModify1::class.java)

        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val weatherId = requireActivity().intent.getIntExtra("weatherId", -1)
        HorizontalScrollFragment2Api(weatherId) // -1은 기본값, 원하는 값으로 설정해주세요

        // weatherId를 활용하여 API 요청 보내기
        if (weatherId != -1) {
            HorizontalScrollFragment2Api(weatherId)
        }
        // 그 외의 UI 초기화나 설정 등을 진행할 수 있습니다.

    }
    private fun HorizontalScrollFragment2Api(weatherId: Int) {
        val memoService = RetrofitImpl.authenticatedRetrofit.create(MemoService::class.java)

        val call = memoService.memoReturn(weatherId = weatherId)

        call.enqueue(object : retrofit2.Callback<BaseResponse<BaseResponse<MemoDailyResponse>>> {
            override fun onResponse(
                call: Call<BaseResponse<BaseResponse<MemoDailyResponse>>>,
                response: Response<BaseResponse<BaseResponse<MemoDailyResponse>>>
            ) {
                if (response.isSuccessful) {
                    val outerResponse = response.body()?.result
                    val memoDailyResponse = outerResponse?.result

                    if (memoDailyResponse != null) {
                        val memoList = memoDailyResponse.memoList

                        // 여기에서 사용자 정보 활용하여 작업 수행
                        if (memoList.isNotEmpty()) {
                            val firstMemoItem = memoList[0]
                            val memoId = firstMemoItem.memoId

                            binding.icHorizontalScroll2Weather.setImageResource(getWeatherIconResource(firstMemoItem.status))
                            val temperature = firstMemoItem.temperature

                            // 여기에서 memoItem의 필드 값을 활용하여 작업 수행
// creationDatetime을 AM/PM 형식으로 표시하기 위해 포맷 변경
                            val creationDatetime = formatDateToAmPm(firstMemoItem.dateTime)
                            binding.tvHorizontalScroll2Time.text = creationDatetime
                            binding.icHorizontalScroll2Weather.setImageResource(
                                getWeatherIconResource(firstMemoItem.status)
                            )
                            // Create entries from the x and y values received from the server
                            val entries: ArrayList<Entry> = ArrayList()

                            for (i in firstMemoItem.dateTime.indices) {
                                val xValue = i.toFloat() // Use index as x value
                                val yValue = firstMemoItem.temperature.toFloat() // Use firstMemoItem.temperature as y value
                                entries.add(Entry(xValue, yValue))
                            }

                            // Create a LineDataSet from the entries
                            val lineDataSet = LineDataSet(entries, "Temperature Data")

                            // Customize the appearance of the LineDataSet
                            lineDataSet.setCircleColor(Color.parseColor("#525252"))
                            lineDataSet.setCircleHoleColor(Color.WHITE)
                            lineDataSet.color = Color.parseColor("#F0A830")
                            lineDataSet.setDrawHorizontalHighlightIndicator(false)
                            lineDataSet.setDrawHighlightIndicators(false)
                            lineDataSet.setDrawValues(false)

                            // Add the LineDataSet to a List of ILineDataSet
                            val dataSets: ArrayList<ILineDataSet> = ArrayList()
                            dataSets.add(lineDataSet)

                            // Create a LineData from the List of ILineDataSet
                            val lineData = LineData(dataSets)

                            // Get the LineChart view from the layout
                            val lineChart: LineChart = binding.chartHorizontalScroll2TempGraph // Make sure to update the ID

                            // Set the LineData to the LineChart
                            lineChart.data = lineData

                            // Customize the appearance of the LineChart
                            lineChart.setDrawBorders(false) // Hide chart borders
                            lineChart.description.isEnabled = false // Hide chart description
                            lineChart.legend.isEnabled = false // Hide chart legend
                            lineChart.xAxis.isEnabled = false // Hide x-axis
                            lineChart.axisLeft.isEnabled = false // Hide left y-axis
                            lineChart.axisRight.isEnabled = false // Hide right y-axis
                            lineChart.axisLeft.setDrawGridLines(false) // Hide horizontal grid lines
                            lineChart.axisRight.setDrawGridLines(false) // Hide horizontal grid lines
                            lineChart.xAxis.setDrawGridLines(false) // Hide vertical grid lines
                            lineChart.setTouchEnabled(false) // Disable chart touch

                            // Hide chart borders (graph frame)
                            lineChart.setDrawBorders(false)

                            // Refresh the LineChart to update the view
                            lineChart.invalidate()

                        }

                    }
                    else {
                        // 서버 응답은 성공했지만 데이터가 없는 경우 처리
                        Log.e("API Response", "No memo data for the requested date")
                    }
                } else {
                    // 서버 응답이 실패한 경우 처리
                    Log.e("API Response", "Failed to retrieve memo data")
                }

            }

            override fun onFailure(
                call: Call<BaseResponse<BaseResponse<MemoDailyResponse>>>,
                t: Throwable
            ) {
                // 네트워크 요청 실패 처리
                Log.e("API Failure", "Error: ${t.message}", t)
                // 사용자에게 오류 메시지 표시
                val errorMessage = "네트워크 요청이 실패했습니다."
            }
        })

    }
    fun formatDateToAmPm(dateTime: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        val outputFormat = SimpleDateFormat("a h:mm", Locale.US)
        val date = inputFormat.parse(dateTime)
        return outputFormat.format(date)
    }

    fun getWeatherIconResource(status: Status): Int {
        return when (status) {
            Status.SUNNY -> R.drawable.ic_sun
            Status.CLOUDY -> R.drawable.ic_cloud
            Status.RAINY -> R.drawable.ic_rain
            Status.LIGHTNING -> R.drawable.ic_thunder
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HorizontalScrollFragment2.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HorizontalScrollFragment2().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}