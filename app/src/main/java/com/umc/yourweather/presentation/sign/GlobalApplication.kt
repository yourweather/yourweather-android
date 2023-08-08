package com.umc.yourweather.presentation.sign

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.umc.yourweather.BuildConfig

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)
        // NaverIdLoginSDK.initialize(this, BuildConfig.NAVER_CLIENT_ID, BuildConfig.NAVER_CLIENT_SECRET, "yourweather")
    }
}
