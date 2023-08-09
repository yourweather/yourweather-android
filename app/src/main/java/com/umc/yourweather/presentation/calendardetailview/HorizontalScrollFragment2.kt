package com.umc.yourweather.presentation.calendardetailview

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
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

        val xValues = arrayOf(1f, 2f, 3f, 4f, 5f) // Replace with your x values
        val yValues = arrayOf(5f, 2f, 7f, 4f, 6f) // Replace with your y values

        val entries: ArrayList<Entry> = ArrayList()
        for (i in xValues.indices) {
            entries.add(Entry(xValues[i], yValues[i]))
        }

        val lineDataSet = LineDataSet(entries, "Data Set 1")
        lineDataSet.setCircleColor(Color.parseColor("#525252"))
        lineDataSet.setCircleHoleColor(Color.WHITE)
        lineDataSet.setColor(Color.parseColor("#F0A830"))
        lineDataSet.setDrawHorizontalHighlightIndicator(false)
        lineDataSet.setDrawHighlightIndicators(false)
        lineDataSet.setDrawValues(false)

        val dataSets: ArrayList<ILineDataSet> = ArrayList()
        dataSets.add(lineDataSet)

        val lineData = LineData(dataSets)

        val lineChart: LineChart = rootView.findViewById(R.id.chart)
        lineChart.data = lineData

        lineChart.setDrawBorders(false)
        lineChart.description.isEnabled = false
        lineChart.legend.isEnabled = false
        lineChart.xAxis.setDrawLabels(false)
        lineChart.axisLeft.setDrawLabels(false)
        lineChart.axisRight.setDrawLabels(false)
        lineChart.axisLeft.setDrawGridLines(false)
        lineChart.axisRight.setDrawGridLines(false)
        lineChart.xAxis.setDrawGridLines(false)
        lineChart.setTouchEnabled(false)
        lineChart.setDrawBorders(false)


        val myTextView1: MaterialTextView = rootView.findViewById(R.id.timeTextView1)
        val myTextView2: MaterialTextView = rootView.findViewById(R.id.timeTextView2)
        val myTextView3: MaterialTextView = rootView.findViewById(R.id.timeTextView3)
        val myTextView4: MaterialTextView = rootView.findViewById(R.id.timeTextView4)
        val myTextView5: MaterialTextView = rootView.findViewById(R.id.timeTextView5)



        myTextView1.paintFlags = myTextView1.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        myTextView2.paintFlags = myTextView1.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        myTextView3.paintFlags = myTextView1.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        myTextView4.paintFlags = myTextView1.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        myTextView5.paintFlags = myTextView1.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        myTextView1.setOnClickListener {
            // Handle click event for myTextView1
        }
        myTextView2.setOnClickListener {
            // Handle click event for myTextView2
        }
        myTextView3.setOnClickListener {
            // Handle click event for myTextView3
        }
        myTextView4.setOnClickListener {
            // Handle click event for myTextView4
        }
        myTextView5.setOnClickListener {
            // Handle click event for myTextView5
        }
        return rootView
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