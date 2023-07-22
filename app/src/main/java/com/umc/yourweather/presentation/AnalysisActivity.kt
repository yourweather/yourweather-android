package com.umc.yourweather.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.databinding.ActivityAnalysisBinding

class AnalysisActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnalysisBinding
    private var selectedButton: androidx.appcompat.widget.AppCompatButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalysisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBell.setOnClickListener {
            val mIntent = Intent(this, UnwrittenDetailListActivity::class.java)
            startActivity(mIntent)
            finish()
        }
    }
}
