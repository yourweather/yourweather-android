package com.umc.yourweather.di

import android.content.Context
import android.content.SharedPreferences

// 로그인에 대한 토큰처리
class TokenSharedPreferences(context: Context) {
    private val prefsFilename = "token_prefs"
    private val key_accessToken = "accessToken"
    private val key_refreshToken = "refreshToken"

    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFilename, 0)

    var accessToken: String?
        get() = prefs.getString(key_accessToken, null)
        set(value) = prefs.edit().putString(key_accessToken, value).apply()

    var refreshToken: String?
        get() = prefs.getString(key_refreshToken, null)
        set(value) = prefs.edit().putString(key_refreshToken, value).apply()

    fun clearTokens() {
        prefs.edit().remove(key_accessToken).remove(key_refreshToken).apply()
    }
}
