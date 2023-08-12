package com.umc.yourweather.data.remote.request

import com.google.gson.annotations.SerializedName

data class ChangeNicknameRequest(
    @SerializedName("change_nickname")
    val nickname: String
)
