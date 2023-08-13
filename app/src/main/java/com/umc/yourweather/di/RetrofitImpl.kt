package com.umc.yourweather.di

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.umc.yourweather.BuildConfig
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.TokenResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
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
            val accessHeader = "Authorization"
            val refreshHeader = "Authorization-refresh"

            var infoRequest =
                createRequestBuilder(request(), accessHeader, App.token_prefs.accessToken)
            var infoResponse = proceed(infoRequest)

//            // 리프래시 test용
//            var infoRequest =
//                createRequestBuilder(request(), accessHeader, "aa")
//            var infoResponse = proceed(infoRequest)

            Log.d("토큰 인터셉터 확인... 첫번째", "${infoResponse.body}")

            // 여기서 오류가 나지 않는다면 그냥 response가 리턴
            if (infoResponse.code == 400) {
                infoResponse.close()

                // 유효하지 않으면 기존 리프래시 붙여서 다시 시도
                infoRequest = createRequestBuilder(request(), refreshHeader, App.token_prefs.refreshToken)
                infoResponse = proceed(infoRequest)

                // 리프래시 붙여서 보낸 요청 값... 잘 왔으면 온 요청 다시 tokenPre에 저장하기....
                // 저장하고 다시 요청
                var refreshResponseObject = parseTokenResponse(infoResponse)
                Log.d("토큰 인터셉터 확인... 두번째... 리프래시 받아오기 시도", "$refreshResponseObject")
                if (refreshResponseObject?.code != 400) {
                    infoResponse.close()

                    Log.d("토큰 인터셉터 확인... 두번째... 리프래시 받아오기 시도성공!", "$refreshResponseObject")
                    // 여기서 오류난다면(리프래시도 썩었으면 ) 리프래시 얻어온게 실패한 response가 돌아간다
                    // 성공하면 다시 토큰 받아온것이므로 그거 다시 저장해주기...
                    App.token_prefs.accessToken = refreshResponseObject?.result?.accessToken
                    App.token_prefs.refreshToken = refreshResponseObject?.result?.refreshToken

                    // 다시 받아온 토큰으로 요청하기~
                    infoRequest = createRequestBuilder(request(), accessHeader, App.token_prefs.accessToken)
                    return proceed(infoRequest)
                    // Log.d("토큰 인터셉터 확인... 갱신된 토큰으로 재요청", "$infoRequest")
                }
            }
            infoResponse
            // 리프래시 오면 다시 시도
            // Log.d("토큰 인터셉터 확인... 최종 response", "${infoResponse.body}")
        }
    }

    private fun createRequestBuilder(request: Request, headers: String, token: String?): Request {
        return request.newBuilder()
            .addHeader("accept", "application/hal+json")
            .addHeader("Content-Type", "application/json")
            .addHeader("$headers", "Bearer $token").build()
    }

    fun parseTokenResponse(response: Response): BaseResponse<TokenResponse>? {
        val responseBody = response.body

        if (responseBody != null) {
            try {
                val source = responseBody.source()
                val buffer = Buffer()
                source.readAll(buffer)
                val responseBodyString = buffer.readUtf8()

                val myResponseType = object : TypeToken<BaseResponse<TokenResponse>>() {}.type
                return Gson().fromJson(responseBodyString, myResponseType)
            } catch (e: IOException) {
                // IOException 처리
                e.printStackTrace()
                // 예외 처리 후 처리할 로직 또는 오류 반환
            }
        }
        return BaseResponse(false, 400, "", null) // 빈 응답 반환 또는 오류 처리
    }

//    fun parseTokenResponse(response: Response): BaseResponse<TokenResponse>? {
//        val responseBody = response.body?.string()
//
//        if (response != null) {
//            Log.d("와이.... ", "$responseBody")
//        } // 로그로 responseBody 출력
//
//        val myResponseType = object : TypeToken<BaseResponse<TokenResponse>>() {}.type
//        return Gson().fromJson(responseBody, myResponseType)
//    }
}
