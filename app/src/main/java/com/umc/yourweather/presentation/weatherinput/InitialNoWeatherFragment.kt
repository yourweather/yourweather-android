
package com.umc.yourweather.presentation.weatherinput

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.yourweather.R
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.UserResponse
import com.umc.yourweather.data.service.UserService
import com.umc.yourweather.databinding.FragmentInitialNoWeatherBinding
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.di.UserSharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InitialNoWeatherFragment : Fragment() {
    private lateinit var binding: FragmentInitialNoWeatherBinding
    private val userService: UserService by lazy {
        RetrofitImpl.authenticatedRetrofit.create(UserService::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentInitialNoWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnInitialWeather.setOnClickListener {
            val newFragment = HomeWeatherInputFragment()
            val transaction = parentFragmentManager.beginTransaction()

            // 기존 프래그먼트를 백스택에서 제거
            parentFragmentManager.popBackStack()

            // 새로운 프래그먼트를 컨테이너에 추가
            transaction.add(R.id.fl_initial_l1, newFragment)

            transaction.addToBackStack(null)
            transaction.commit()
        }

        // 마이페이지 api호출로 닉네임 값 변경
        userService.getMyPage().enqueue(object : Callback<BaseResponse<UserResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<UserResponse>>,
                response: Response<BaseResponse<UserResponse>>,
            ) {
                if (response.isSuccessful) {
                    val userResponse = response.body()?.result
                    val userNickname = userResponse?.nickname ?: "유저 닉네임"
                    binding.tvInitialMsg1.text = "$userNickname 님의 감정 날씨를"

                    // 닉네임 SH 저장
                    UserSharedPreferences.setUserNickname(requireContext(), userNickname)
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("Initial", "응답 실패")
                }
            }

            override fun onFailure(call: Call<BaseResponse<UserResponse>>, t: Throwable) {
                Log.e("Initial", "API호출 실패")
            }
        })
    }
}
