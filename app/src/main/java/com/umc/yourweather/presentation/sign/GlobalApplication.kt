package com.umc.yourweather.presentation.sign

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK
import com.umc.yourweather.BuildConfig
import com.umc.yourweather.util.PreferenceUtil

class GlobalApplication : Application() {
    companion object {
        lateinit var tokenPrefs: PreferenceUtil
    }

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)
        NaverIdLoginSDK.initialize(this, BuildConfig.NAVER_CLIENT_ID, BuildConfig.NAVER_CLIENT_SECRET, "yourweather")
        // prefs = PreferenceUtil(applicationContext)
    }
}
