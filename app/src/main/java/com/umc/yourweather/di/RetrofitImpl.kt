package com.umc.yourweather.di

import com.umc.yourweather.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object RetrofitImpl {
    private const val BASE_URL = BuildConfig.BASE_URL

    // 토큰이 필요하지 않은 OkHttpClient
    private val nonOkHttpClient: OkHttpClient by lazy {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(NonAppInterceptor())
            .build()
    }

    // 토큰이 필요한 OkHttpClient
    private val authenticatedOkHttpClient: OkHttpClient by lazy {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(AuthenticatedInterceptor())
            .build()
    }

    // 토큰이 필요하지 않은 Retrofit
    val nonRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(nonOkHttpClient)
            .baseUrl(BASE_URL)
            .build()
    }

    // 토큰이 필요한 Retrofit
    val authenticatedRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(authenticatedOkHttpClient)
            .baseUrl(BASE_URL)
            .build()
    }

    // 토큰이 필요하지 않은 요청에 사용할 인터셉터
    class NonAppInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
            val requestBuilder = request().newBuilder()
                .addHeader("accept", "application/hal+json")
                .addHeader("Content-Type", "application/json")

            val newRequest = requestBuilder.build()
            proceed(newRequest)
        }
    }

    // 토큰이 필요한 요청에 사용할 인터셉터
    class AuthenticatedInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
            val requestBuilder = request().newBuilder()
                .addHeader("accept", "application/hal+json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer ${App.token_prefs.accessToken}")

            val newRequest = requestBuilder.build()
            proceed(newRequest)
        }
    }
}
