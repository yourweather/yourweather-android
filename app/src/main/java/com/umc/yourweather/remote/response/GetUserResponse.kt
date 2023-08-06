package com.umc.yourweather.remote.response

data class GetUserResponse(
    val success: Boolean,
    val code: Int,
    val message: String,
    val result: UserResponse
){
    data class UserResponse(
        val nickname: String,
        val email: String
    )
}
