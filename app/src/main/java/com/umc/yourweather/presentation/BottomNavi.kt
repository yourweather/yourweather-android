
package com.umc.yourweather.presentation

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.umc.yourweather.R
import com.umc.yourweather.presentation.analysis.AnalysisFragment
import com.umc.yourweather.presentation.calendar.CalendarTotalViewFragment
import com.umc.yourweather.presentation.weatherinput.InitialNoWeatherFragment

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
                R.id.bnv_report -> replaceFragment(AnalysisFragment())
                else -> replaceFragment(InitialNoWeatherFragment()) //마이페이지 첫 뷰 생성시 변경 예정(우선 홈화면으로 해둠)
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
