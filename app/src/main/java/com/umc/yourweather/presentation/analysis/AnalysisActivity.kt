package com.umc.yourweather.presentation.analysis

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.R
import com.umc.yourweather.R.drawable
import com.umc.yourweather.databinding.ActivityAnalysisBinding
import com.umc.yourweather.presentation.BarStaticsMonthlyFragment
import com.umc.yourweather.presentation.BarStaticsWeeklyFragment

@Suppress("DEPRECATION")
class AnalysisActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnalysisBinding
    private var isMonthlySelected = true
    private var isWeeklySelected = false

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalysisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val normalBackground: Drawable = resources.getDrawable(drawable.btn_brown_rec)
        val pressedBackground: Drawable = resources.getDrawable(drawable.btn_transp_rec)

        viewMonthly()

        binding.btnAnalysisMonthly.setOnClickListener {
            if (isMonthlySelected) {
                return@setOnClickListener // Already selected, do nothing
            }
            isMonthlySelected = true
            isWeeklySelected = false

            viewMonthly()

            if (binding.btnAnalysisMonthly.background == normalBackground) {
                binding.btnAnalysisMonthly.background = pressedBackground
                binding.btnAnalysisMonthly.setTextColor(resources.getColor(android.R.color.black))
                binding.btnAnalysisWeekly.background = normalBackground
                binding.btnAnalysisWeekly.setTextColor(resources.getColor(android.R.color.white))
            } else {
                binding.btnAnalysisMonthly.background = normalBackground
                binding.btnAnalysisWeekly.background = pressedBackground
                binding.btnAnalysisMonthly.setTextColor(resources.getColor(android.R.color.white))
                binding.btnAnalysisWeekly.setTextColor(resources.getColor(android.R.color.black))
            }
        }

        binding.btnAnalysisWeekly.setOnClickListener {
            if (isWeeklySelected) {
                return@setOnClickListener // Already selected, do nothing
            }

            isWeeklySelected = true
            isMonthlySelected = false
            viewWeekly()

            if (binding.btnAnalysisWeekly.background === normalBackground) {
                binding.btnAnalysisWeekly.background = pressedBackground
                binding.btnAnalysisWeekly.setTextColor(resources.getColor(android.R.color.black))
                binding.btnAnalysisMonthly.background = normalBackground
                binding.btnAnalysisMonthly.setTextColor(resources.getColor(android.R.color.white))
            } else {
                binding.btnAnalysisWeekly.background = normalBackground
                binding.btnAnalysisWeekly.setTextColor(resources.getColor(android.R.color.white))
                binding.btnAnalysisMonthly.background = pressedBackground
                binding.btnAnalysisMonthly.setTextColor(resources.getColor(android.R.color.black))
            }
        }

        binding.btnBell.setOnClickListener {
            val mIntent = Intent(this, UnwrittenDetailListActivity::class.java)
            startActivity(mIntent)
        }
    }

    private fun viewMonthly() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.f_barStatic, BarStaticsMonthlyFragment())
        transaction.replace(R.id.f_iconStatic, IconStaticsMonthlyFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun viewWeekly() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.f_barStatic, BarStaticsWeeklyFragment())
        transaction.replace(R.id.f_iconStatic, IconStaticsWeeklyFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
