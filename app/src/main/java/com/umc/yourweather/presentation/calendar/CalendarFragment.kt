package com.umc.yourweather.presentation.calendar

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.MonthResponse
import com.umc.yourweather.data.remote.response.MonthWeatherResponse
import com.umc.yourweather.data.service.WeatherService
import com.umc.yourweather.databinding.FragmentCalendarBinding
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.presentation.sign.SignInActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class CalendarFragment : Fragment() {
    lateinit var binding: FragmentCalendarBinding
    var dateList: MutableList<LocalDate> = mutableListOf()
    lateinit var weatherData: List<MonthWeatherResponse>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCalendarBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        val year = arguments?.getInt("year")
        val month = arguments?.getInt("month")
        val id = arguments?.getString("id")

        getDate(year!!, month!!)
        // Log.d("날짜확인확인", "$year $month")
        monthWeatherApi(year, month)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDate(year: Int, month: Int) {
        val firstDay = LocalDate.of(year, month, 1)
        // 이번 달 첫날의 요일 (월요일: 1, 화요일: 2, ..., 일요일: 7)
        val dayOfWeekValue = firstDay.dayOfWeek.value
        var n = 0

        if (dayOfWeekValue != 7) {
            n = dayOfWeekValue
        }

        var startDate = firstDay.minusDays(n.toLong())
        for (i in 1..42) {
            // Log.d("날짜확인확인", "${startDate.year} ${startDate.monthValue}, ${startDate.dayOfMonth}")
            dateList.add(startDate)
            startDate = startDate.plusDays(1)
        }
    }

    fun monthWeatherApi(year: Int, month: Int) {
        val service = RetrofitImpl.authenticatedRetrofit.create(WeatherService::class.java)

        Log.d("Calendar", "요청시작")
        service.getMonthData(year = year, month = month)
            .enqueue(object : Callback<BaseResponse<MonthResponse>> {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(
                    call: Call<BaseResponse<MonthResponse>>,
                    response: Response<BaseResponse<MonthResponse>>,
                ) {
                    val weatherResponse = response.body()
                    val code = response.body()?.code
                    if (response.isSuccessful) {
                        if (code == 200) {
                            weatherData = weatherResponse?.result?.weatherList ?: emptyList()
                            binding.ctCalendarCustom.initCalendar(year!!, month!!, dateList, weatherData)

                            Log.d("Calendar ", "$weatherData")
                            Log.d("Calendar ", "$weatherResponse")
                        }
                    } else {
                        weatherData = emptyList()
                        binding.ctCalendarCustom.initCalendar(year!!, month!!, dateList, weatherData)
                        Log.d(
                            "Calendar",
                            "onResponse 오류: ${response?.toString()}",
                        )
                    }
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFailure(call: Call<BaseResponse<MonthResponse>>, t: Throwable) {
                    Toast.makeText(requireContext(), "네트워크 오류입니다. 인터넷 연결을 확인해주세요!", Toast.LENGTH_LONG).show()
//                    weatherData = emptyList()
//                    binding.ctCalendarCustom.initCalendar(year!!, month!!, dateList, weatherData)
                    Log.d("Calendar", "onFailure 에러: " + t.message.toString())
                    val mIntent = Intent(requireActivity(), SignInActivity::class.java)
                    requireActivity().startActivity(mIntent)
                    // 현재 액티비티 종료
                    requireActivity().finish()
                }
            })
    }
}
