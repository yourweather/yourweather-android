package com.umc.yourweather.util

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.umc.yourweather.R
import com.umc.yourweather.data.enums.Status

class ResourceUtils {
    companion object {
        fun setWeatherIc(context: Context, weather: Status): Drawable? {
            when (weather) {
                Status.SUNNY -> {
                    return ContextCompat.getDrawable(context, R.drawable.ic_sun)
                }
                Status.CLOUDY -> {
                    return ContextCompat.getDrawable(context, R.drawable.ic_cloud)
                }
                Status.LIGHTNING -> {
                    return ContextCompat.getDrawable(context, R.drawable.ic_thunder)
                }
                Status.RAINY -> {
                    return ContextCompat.getDrawable(context, R.drawable.ic_rain)
                }
            }
        }
    }
}
