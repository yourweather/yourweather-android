package com.umc.yourweather.di

import android.app.Application
import android.content.Context
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK
import com.umc.yourweather.BuildConfig

class App : Application() {
    companion object {
        var appContext: Context? = null
        lateinit var token_prefs: TokenSharedPreferences
    }

    override fun onCreate() {
        super.onCreate()
        token_prefs = TokenSharedPreferences(applicationContext)

        appContext = this
        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)
        NaverIdLoginSDK.initialize(this, BuildConfig.NAVER_CLIENT_ID, BuildConfig.NAVER_CLIENT_SECRET, "yourweather")
    }
}
