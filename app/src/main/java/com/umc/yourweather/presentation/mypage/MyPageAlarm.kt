package com.umc.yourweather.presentation.mypage

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivityMyPageAlarmBinding
import com.umc.yourweather.di.UserSharedPreferences

class MyPageAlarm : AppCompatActivity() {
    private lateinit var binding: ActivityMyPageAlarmBinding
    private val notificationId = 1
    private val PREFS_NAME = "MyPageAlarmPrefs"
    private val PREF_SWITCH_MONTHLY = "switchMonthly"
    private val PREF_SWITCH_WEEKLY = "switchWeekly"

    companion object {
        const val CHANNEL_ID = "유어웨더"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAlarmBack.setOnClickListener {
            finish()
        }
        binding.flMypageAlarmBackbtn.setOnClickListener {
            finish()
        }


        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isMonthlyChecked = prefs.getBoolean(PREF_SWITCH_MONTHLY, false)
        val isWeeklyChecked = prefs.getBoolean(PREF_SWITCH_WEEKLY, false)
        binding.swMonthly.isChecked = isMonthlyChecked
        binding.swWeekly.isChecked = isWeeklyChecked

        binding.swMonthly.setOnCheckedChangeListener { _, isChecked ->
            val editor = prefs.edit()
            editor.putBoolean(PREF_SWITCH_MONTHLY, isChecked)
            editor.apply()

            if (isChecked) {
                createNotificationChannelMonthly()
                setAlarmMonthly()
            } else {
                cancelAlarm()
            }
        }

        binding.swWeekly.setOnCheckedChangeListener { _, isChecked ->
            val editor = prefs.edit()
            editor.putBoolean(PREF_SWITCH_WEEKLY, isChecked)
            editor.apply()

            if (isChecked) {
                createNotificationChannelWeekly()
                setAlarmWeekly()
            } else {
                cancelAlarm()
            }
        }
    }

    // 알림 채널 생성 함수
    private fun createNotificationChannelWeekly() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelName = "주간 비교 분석" // 알림 이름
            val channelDescription = "감정날씨 주간 비교 분석 알림" // 알림 설명
            val importance = NotificationManager.IMPORTANCE_HIGH

            val notificationChannel = NotificationChannel(CHANNEL_ID, channelName, importance)
            notificationChannel.description = channelDescription

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.parseColor("#F07818")
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(100L, 200L, 300L)

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun createNotificationChannelMonthly() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelName = "월간 비교 분석" // 알림 이름
            val channelDescription = "감정날씨 월간 비교 분석 알림" // 알림 설명
            val importance = NotificationManager.IMPORTANCE_HIGH

            val notificationChannel = NotificationChannel(CHANNEL_ID, channelName, importance)
            notificationChannel.description = channelDescription

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.parseColor("#F07818")
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(100L, 200L, 300L)

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun setAlarmMonthly() {
        val userNickname = UserSharedPreferences.getUserNickname(this)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.img_yourweatherlogo)
            .setContentTitle("$userNickname 님의 이번 달은 어떠셨나요?")
            .setContentText("지금 바로 감정날씨 월간 분석 결과를 확인해보세요.")
            .setPriority(NotificationCompat.PRIORITY_MAX)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, builder.build())
    }

    private fun setAlarmWeekly() {
        val userNickname = UserSharedPreferences.getUserNickname(this)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.img_yourweatherlogo)
            .setContentTitle("$userNickname 님의 이번 주는 어떠셨나요?")
            .setContentText("지금 바로 감정날씨 주간 분석 결과를 확인해보세요.")
            .setPriority(NotificationCompat.PRIORITY_MAX)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, builder.build())
    }

    private fun cancelAlarm() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }
}
