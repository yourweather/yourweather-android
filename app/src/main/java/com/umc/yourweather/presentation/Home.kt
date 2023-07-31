package com.umc.yourweather.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivityHomeBinding

class Home : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Glide.with(this).load(R.raw.motion_home_sun).into(findViewById(R.id.motion_home_weather))

        binding.btnHomeWeatherinput.setOnClickListener {
            setFragment()
        }
    }

    private fun setFragment() {
        val transaction = supportFragmentManager.beginTransaction()
            .add(R.id.fl_home_l1, HomeWeatherInputFragment())
            .addToBackStack(null)
        transaction.commit()
    }
    fun goToNewHome() {
        showHomeToast()
        supportFragmentManager.popBackStack()
    }
    private fun showHomeToast() {
        val customToastView = LayoutInflater.from(this).inflate(R.layout.toast_home, null)

        val homeToast = Toast(this)
        homeToast.view = customToastView

        homeToast.duration = Toast.LENGTH_LONG

        homeToast.setGravity(android.view.Gravity.BOTTOM or android.view.Gravity.CENTER, 0, resources.getDimensionPixelSize(R.dimen.home_toast_margin_bottom))

        homeToast.show()
    }
}
