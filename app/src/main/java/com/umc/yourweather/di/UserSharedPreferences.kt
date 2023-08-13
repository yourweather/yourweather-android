package com.umc.yourweather.di

import android.content.Context
import android.content.SharedPreferences

// 자동 로그인을 위한 SharedPreferences
object UserSharedPreferences {
    private val MY_ACCOUNT: String = "account"
    const val USER_PW_TO_STAR = "USER_PW_TO_STAR"
    const val USER_NICKNAME = "USER_NICKNAME"
    const val USER_PLATFORM = "USER_PLATFORM"

    fun setUserPwToStar(context: Context, input: String) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()

        editor.putString(USER_PW_TO_STAR, input.replace(".", "*"))
        editor.commit()
    }

    fun getUserPwToStar(context: Context): String {
        val prefs: SharedPreferences =
            context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString(USER_PW_TO_STAR, "").toString()
    }

    fun setUserNickname(context: Context, input: String) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString(USER_NICKNAME, input)
        editor.commit()
    }

    fun getUserNickname(context: Context): String {
        val prefs: SharedPreferences =
            context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString(USER_NICKNAME, "").toString()
    }
    fun getUserPw(context: Context): String {
        val prefs: SharedPreferences =
            context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString("MY_PW", "").toString()
    }
    fun setUserPlatform(context: Context, input: String) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString(USER_PLATFORM, input)
        editor.commit()
    }

    fun getUserPlatform(context: Context): String {
        val prefs: SharedPreferences =
            context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString(USER_PLATFORM, "").toString()
    }

    fun clearUser(context: Context) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.commit()
    }
}
