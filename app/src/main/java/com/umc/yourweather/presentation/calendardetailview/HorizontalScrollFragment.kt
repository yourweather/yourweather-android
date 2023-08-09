package com.umc.yourweather.presentation.calendardetailview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umc.yourweather.R
import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class HorizontalScrollFragment : Fragment() {
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
        val rootView = inflater.inflate(R.layout.fragment_horizontal_scroll, container, false)

        // Assume you received x and y values from the server in the form of arrays
        val xValues = arrayOf(1f, 2f, 3f, 4f, 5f) // Replace with your x values
        val yValues = arrayOf(5f, 2f, 7f, 4f, 6f) // Replace with your y values

        // Create entries from the x and y values received from the server
        val entries: ArrayList<Entry> = ArrayList()
        for (i in xValues.indices) {
            entries.add(Entry(xValues[i], yValues[i]))
        }

        // Create a LineDataSet from the entries
        val lineDataSet = LineDataSet(entries, "Data Set 1")

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
        val lineChart: LineChart = rootView.findViewById(R.id.chart) // Make sure to update the ID

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

        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HorizontalScrollFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HorizontalScrollFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}