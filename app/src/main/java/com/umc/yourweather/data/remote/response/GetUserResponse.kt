package com.umc.yourweather.data.remote.response

data class GetUserResponse(
    val success: Boolean,
    val code: Int,
    val message: String,
    val result: com.umc.yourweather.data.remote.response.GetUserResponse.UserResponse
){
    data class UserResponse(
        val nickname: String,
        val email: String
    )
}
