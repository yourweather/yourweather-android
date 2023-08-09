package com.umc.yourweather.di

import com.umc.yourweather.BuildConfig
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitImpl {

    private const val BASE_URL = BuildConfig.BASE_URL

    // Retrofit without token
    private val nonTokenHttpClient: OkHttpClient
        get() {
            val loggingInterceptor = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)

            return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
        }

    private val nonTokenRetrofit: Retrofit
        get() {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(nonTokenHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

    // Retrofit with token
    private val tokenHttpClient: OkHttpClient
        get() {
            val loggingInterceptor = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)

            return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(TokenInterceptor())
                .build()
        }

    val tokenRetrofit: Retrofit
        get() {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(tokenHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

    val retrofit: Retrofit
        get() {
            return if (App.token_prefs.accessToken != null) {
                tokenRetrofit
            } else {
                nonTokenRetrofit
            }
        }

    // Custom interceptor for adding token to headers
    private class TokenInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val originalRequest = chain.request()
            val newRequest = originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer ${App.token_prefs.accessToken}")
                .build()
            return chain.proceed(newRequest)
        }
    }
}
