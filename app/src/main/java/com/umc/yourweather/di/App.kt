package com.umc.yourweather.di

import android.app.Application
import android.content.Context
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK
import com.umc.yourweather.BuildConfig

class App : Application() {
    companion object {
        lateinit var appContext: Context
        lateinit var token_prefs: TokenSharedPreferences
        lateinit var globalNickname: String
    }

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)
        NaverIdLoginSDK.initialize(this, BuildConfig.NAVER_CLIENT_ID, BuildConfig.NAVER_CLIENT_SECRET, "yourweather")
        token_prefs = TokenSharedPreferences(applicationContext)
        appContext = this

        globalNickname = ""
    }
}
