package com.umc.yourweather.di


import com.umc.yourweather.data.service.UserService


object RefreshTokenService {

    private val userService = RetrofitImpl.tokenRetrofit.create(UserService::class.java)

    fun refreshToken(): String {
        val res = userService.getNewToken().execute()
        if (res.isSuccessful) {
            val newAccessToken = res.body()?.accessToken ?: ""
            val newRefreshToken = res.body()?.refreshToken ?: ""
            if (newAccessToken.isNotBlank()) {
                App.token_prefs.accessToken = newAccessToken
                App.token_prefs.refreshToken = newRefreshToken
                return newAccessToken
            }
        }
        throw IllegalStateException("토큰 재발급 실패")
    }
}
