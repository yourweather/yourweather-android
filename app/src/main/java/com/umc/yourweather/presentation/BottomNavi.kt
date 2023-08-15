
package com.umc.yourweather.presentation

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.umc.yourweather.R
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.MonthResponse
import com.umc.yourweather.data.remote.response.MonthWeatherResponse
import com.umc.yourweather.data.service.WeatherService
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.presentation.analysis.AnalysisFragment
import com.umc.yourweather.presentation.calendar.CalendarTotalViewFragment
import com.umc.yourweather.presentation.mypage.MyPageFragment
import com.umc.yourweather.presentation.weatherinput.HomeFragment
import com.umc.yourweather.presentation.weatherinput.InitialNoWeatherFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class BottomNavi : AppCompatActivity() {
    private val fl: FrameLayout by lazy {
        findViewById(R.id.fl_content)
    }

    private val bn: BottomNavigationView by lazy {
        findViewById(R.id.bnv_main)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bnv)
        var isClickInProgress = false // 클릭 진행 중 여부를 저장하는 변수

        // false 반환시 입력값 없음 -> 오류났을 때 무조건 false반환하게
        // true 반환시 입렧값 있음

        if (savedInstanceState == null) {
            CoroutineScope(Dispatchers.Main).launch {
                val result = isTodayRecordExist()
                if (result) {
                    Log.d("코루틴 반환값 확인", "$result")
                    Log.d("코루틴 시작 메인함수 ", "결과옴")
                    Log.d("코루틴 시작 메인함수", "프래그먼트 시작")
                    supportFragmentManager.beginTransaction()
                        .replace(fl.id, HomeFragment())
                        .commit()
                } else {
                    Log.d("코루틴 시작 메인함수 ", "결과옴")
                    Log.d("코루틴 시작 메인함수", "프래그먼트")
                    supportFragmentManager.beginTransaction()
                        .replace(fl.id, InitialNoWeatherFragment())
                        .commit()
                }
            }
        }

        bn.setOnItemSelectedListener {
            if (!isClickInProgress) { // 클릭 진행 중이 아닐 때만 실행, 광클 시 앱 종료 방지
                isClickInProgress = true // 클릭 시작
                when (it.itemId) {
                    R.id.bnv_home -> {
                        CoroutineScope(Dispatchers.Main).launch {
                            val result = isTodayRecordExist()
                            Log.d("코루틴 반환값 확인", "$result")
                            Log.d("코루틴 시작 메인함수 ", "결과옴")
                            Log.d("코루틴 시작 메인함수", "프래그먼트 시작")
                            if (result) {
                                replaceFragment(HomeFragment()) // isExistMissedInput가 true일 때 띄울 프래그먼트를 넣으세요.
                            } else {
                                Log.d("코루틴 시작 메인함수 ", "결과옴")
                                Log.d("코루틴 시작 메인함수", "프래그먼트")
                                replaceFragment(InitialNoWeatherFragment())
                            }
                        }
                        isClickInProgress = false
                    }

                    R.id.bnv_calender -> {
                        replaceFragment(CalendarTotalViewFragment())
                        isClickInProgress = false
                    }
                    R.id.bnv_report -> {
                        replaceFragment(AnalysisFragment())
                        isClickInProgress = false
                    }
                    else -> {
                        replaceFragment(MyPageFragment())
                        isClickInProgress = false
                    }
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(fl.id, fragment)
            .commit()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun isTodayRecordExist(): Boolean {
        val service = RetrofitImpl.authenticatedRetrofit.create(WeatherService::class.java)
        var weatherData: List<MonthWeatherResponse> = emptyList()
        val todayDate = LocalDate.now()
        val month = todayDate.monthValue
        val year = todayDate.year
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedTodayDate = todayDate.format(formatter)

        return suspendCancellableCoroutine { continuation ->
            val call = service.getMonthData(year = year, month = month)
            call.enqueue(object : Callback<BaseResponse<MonthResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<MonthResponse>>,
                    response: Response<BaseResponse<MonthResponse>>,
                ) {
                    val weatherResponse = response.body()
                    val code = weatherResponse?.code
                    if (response.isSuccessful) {
                        if (code == 200) {
                            val weatherData = weatherResponse?.result?.weatherList ?: emptyList()
                            Log.d(
                                "로그인, 미입력조회..",
                                "thisDate : ${weatherData.any { it.date == formattedTodayDate }}",
                            )
                            Log.d("코루틴 시작 요청함수 ", "요청시작")
                            Log.d("코루틴 시작 요청함수", "결과옴")
                            val isTodayRecordExist = weatherData.any { it.date == formattedTodayDate }

                            Log.d(
                                "코루틴 시작 요청함수",
                                "$isTodayRecordExist $formattedTodayDate $weatherData}",

                            )
                            continuation.resume(isTodayRecordExist, null)
                        } else {
                            Log.d(
                                "Calendar",
                                "onResponse 에러: " + weatherResponse?.message.toString(),
                            )
                        }
                    } else {
                        Log.d("Calendar", "onFailure 에러: " + weatherResponse?.message.toString())
                    }
                }

                override fun onFailure(call: Call<BaseResponse<MonthResponse>>, t: Throwable) {
                    Log.d("Calendar", "onFailure 에러: " + t.message.toString())
                    continuation.resume(false, null)
                }
            })
        }
    }
}
