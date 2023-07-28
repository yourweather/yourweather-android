package com.umc.yourweather.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.R.drawable
import com.umc.yourweather.databinding.ActivityAnalysisBinding

@Suppress("DEPRECATION")
class AnalysisActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnalysisBinding

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalysisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val normalBackground: Drawable = resources.getDrawable(drawable.btn_transp_select_rec)
        val pressedBackground: Drawable = resources.getDrawable(drawable.btn_brown_select_rec)

        binding.btnAnalysisMonthly.setOnClickListener {
            if (binding.btnAnalysisMonthly.background === normalBackground) {
                binding.btnAnalysisMonthly.background = pressedBackground
                binding.btnAnalysisWeekly.background = normalBackground
            } else {
                binding.btnAnalysisMonthly.background = normalBackground
                binding.btnAnalysisWeekly.background = pressedBackground
            }
        }

        binding.btnAnalysisWeekly.setOnClickListener {
            if (binding.btnAnalysisWeekly.background === normalBackground) {
                binding.btnAnalysisWeekly.background = pressedBackground
                binding.btnAnalysisMonthly.background = normalBackground
            } else {
                binding.btnAnalysisWeekly.background = normalBackground
                binding.btnAnalysisMonthly.background = pressedBackground
            }
        }

        binding.btnBell.setOnClickListener {
            val mIntent = Intent(this, UnwrittenDetailListActivity::class.java)
            startActivity(mIntent)
            finish()
        }
    }
}
