
package com.umc.yourweather.presentation

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.umc.yourweather.R
import com.umc.yourweather.presentation.analysis.AnalysisFragment

class BottomNavi : AppCompatActivity() {
    private val fl: FrameLayout by lazy {
        findViewById(R.id.fl_content)
    }

    private val bn: BottomNavigationView by lazy {
        findViewById(R.id.bnv_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bnv)

        supportFragmentManager.beginTransaction().add(fl.id, BnvHome()).commit()

        bn.setOnItemSelectedListener {
            replaceFragment(
                when (it.itemId) {
                    R.id.bnv_home -> BnvHome()
//                    R.id.bnv_calender -> BnvCalender()
                    R.id.bnv_calender -> CalendarTotalViewFragment()
                    R.id.bnv_report -> AnalysisFragment()
                    else -> BnvMyPage()
                },
            )
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(fl.id, fragment).commit()
    }
}
