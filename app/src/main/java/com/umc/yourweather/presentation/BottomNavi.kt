
package com.umc.yourweather.presentation

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.umc.yourweather.R
import com.umc.yourweather.presentation.calendar.CalendarTotalViewFragment

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

        // 최초 한 번만 트랜잭션을 시작
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(fl.id, InitialNoWeatherFragment())
                .commit()
        }

        bn.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bnv_home -> replaceFragment(InitialNoWeatherFragment())
//                R.id.bnv_calender -> replaceFragment(BnvCalender())
                R.id.bnv_calender -> replaceFragment(CalendarTotalViewFragment())
                R.id.bnv_report -> replaceFragment(BnvReport())
                else -> replaceFragment(BnvMyPage())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(fl.id, fragment)
            .commit()
    }
}
