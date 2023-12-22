package com.umc.yourweather.di

import android.content.Context
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
            .addInterceptor(AuthenticatedInterceptor(App.appContext))
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
    class AuthenticatedInterceptor(val context: Context) : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
            val accessHeader = "Authorization"
            val refreshHeader = "Authorization-refresh"

            var infoRequest =
                createRequestBuilder(request(), accessHeader, App.token_prefs.accessToken)

            var infoResponse = proceed(infoRequest)

            Log.d("저장된 토큰 확인", "저장 토큰 확인 액세스 ${App.token_prefs.accessToken}")
            Log.d("저장된 토큰 확인", "저장 토큰 확인 리프래시 ${App.token_prefs.refreshToken}")
            Log.d("토큰 인터셉터 확인", "첫번재 요청 리턴 코드 ${infoResponse.code}")

            // 여기서 오류가 나지 않는다면 그냥 response가 리턴
            if (infoResponse.code == 400) {
                infoResponse.close()

                Log.d("토큰 인터셉터 확인", "두번째... 리프래시 붙여서 토큰 갱신하기 시도")
                // 유효하지 않으면 기존 리프래시 붙여서 다시 시도
                infoRequest =
                    createRequestBuilder(request(), refreshHeader, App.token_prefs.refreshToken)
                infoResponse = proceed(infoRequest)
                Log.d("토큰 인터셉터 확인", "두번재 요청 리턴 코드 ${infoResponse.code}")

                var refreshResponseObject = parseTokenResponse(infoResponse)
                Log.d("토큰 인터셉터 확인", "두번재 요청 리턴 코드 파싱된것으로 확인하기${ refreshResponseObject?.code}")

                if (refreshResponseObject?.code != 400) {
                    Log.d("토큰 인터셉터 확인", "토큰 갱신 시도성공!")

                    val newAccessToken = refreshResponseObject?.result?.accessToken
                    val newRefreshToken = refreshResponseObject?.result?.refreshToken
//                    infoResponse.close()

                    Log.d("토큰 인터셉터 확인", "갱신 요청. 응답 확인. 파싱된값 $refreshResponseObject?")
                    Log.d("토큰 인터셉터 확인", "갱신 요청. 응답 확인. ${infoResponse.body}")
                    Log.d("토큰 인터셉터 확인", "갱신 요청. 토큰 확인. 액세스 $newAccessToken")
                    Log.d("토큰 인터셉터 확인", "갱신 요청. 토큰 확인. 리프래시 $newRefreshToken")

                    // 받은 토큰이 null이 아니라면 토큰 저장하고 다시 헤더에 붙여서 재요청함.
                    if (newAccessToken != null && newRefreshToken != null) {
                        Log.d("토큰 인터셉터 확인", "새 토큰으로 다시 요청")
                        App.token_prefs.accessToken = newAccessToken
                        App.token_prefs.refreshToken = newRefreshToken
                        val newRequest = createRequestBuilder(
                            request(),
                            accessHeader,
                            App.token_prefs.accessToken,
                        )
                        val newResponse = proceed(newRequest)
                        Log.d("토큰 인터셉터 확인", "세번째 요청 리턴 코드 ${newResponse.code}")

                        return newResponse
//                        if (newResponse.code == 400) {
//                            Log.d("토큰 인터셉터 확인", "재요청 실패. 토큰 오류입니다.")
//                            UserSharedPreferences.clearUser(context) // 로그아웃
//                            App.token_prefs.clearTokens() // 토큰 제거
//                            Handler(Looper.getMainLooper()).post {
//                                Toast.makeText(
//                                    context,
//                                    "다시 로그인해주세요! ",
//                                    Toast.LENGTH_SHORT,
//                                ).show()
//                                val intent =
//                                    Intent(context, SignInActivity::class.java) // 로그인 화면으로 이동
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                                context.startActivity(intent)
//                            }
//                        } else {
//                            return newResponse
//                        }
                    }
                    Log.d("토큰 인터셉터 확인", "토큰 요청으로 받은 토큰이 null입니다.")
                }
            }
            infoResponse
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
