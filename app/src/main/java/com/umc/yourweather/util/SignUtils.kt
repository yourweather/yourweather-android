package com.umc.yourweather.util

class SignUtils {
    companion object {
        const val KAKAOTAG = "카카오소셜로그인"

        fun isValidPassword(password: String): Boolean {
            val passwordPattern = "^(?=.*[a-zA-Z])(?=.*\\d).{8,}$"
            return password.matches(passwordPattern.toRegex())
        }

//        fun changeVisible(visibility: Int): Int {
//            return if (visibility == View.VISIBLE) {
//                View.INVISIBLE
//            } else {
//                View.VISIBLE
//            }
//        }
    }
}
