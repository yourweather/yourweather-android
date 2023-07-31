package com.umc.yourweather.util

import android.view.View

class SignUtils {
    companion object {
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
