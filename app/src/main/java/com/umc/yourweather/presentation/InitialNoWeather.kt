
package com.umc.yourweather.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.R

class InitialNoWeather : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial_no_weather)

        val customToastView = LayoutInflater.from(this).inflate(R.layout.toast_initial, null)

        val initialToast = Toast(this)
        initialToast.view = customToastView

        initialToast.duration = Toast.LENGTH_LONG

        initialToast.setGravity(android.view.Gravity.BOTTOM or android.view.Gravity.CENTER, 0, resources.getDimensionPixelSize(R.dimen.initial_toast_margin_bottom))

        initialToast.show()
    }
}