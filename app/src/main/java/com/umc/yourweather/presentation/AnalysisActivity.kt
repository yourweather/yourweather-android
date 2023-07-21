package com.umc.yourweather.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivityAnalysisBinding

class AnalysisActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnalysisBinding
    private var selectedButton: androidx.appcompat.widget.AppCompatButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalysisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set the initial state
        setSelectedButton(binding.btnAnalysisWeekly)

        // Set click listeners
        binding.btnAnalysisWeekly.setOnClickListener {
            setSelectedButton(binding.btnAnalysisWeekly)
        }

        binding.btnAnalysisMonthly.setOnClickListener {
            setSelectedButton(binding.btnAnalysisMonthly)
        }

        binding.btnBell.setOnClickListener {
            val mIntent = Intent(this, UnwrittenDetailListActivity::class.java)
            startActivity(mIntent)
            finish()
        }
    }

    private fun setSelectedButton(button: androidx.appcompat.widget.AppCompatButton) {
        selectedButton?.apply {
            setBackgroundResource(R.drawable.bg_btn_report_select_rec)
            setTextColor(resources.getColor(R.color.black))
        }

        button.apply {
            setBackgroundResource(R.drawable.bg_btn_report_select_rec_colored)
            setTextColor(resources.getColor(R.color.white))
        }

        selectedButton = button
    }
}
