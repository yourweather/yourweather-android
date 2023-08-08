package com.umc.yourweather.di

import com.umc.yourweather.data.service.UserService
// import com.umc.yourweather.di.RetrofitImpl.retrofit


object RefreshTokenService {
/* userservie 내에 토근 발급 관련 함수 getNewtoken 추가한 이후 주석 없애기
    private val userService = retrofit.create(UserService::class.java)

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
 */
}