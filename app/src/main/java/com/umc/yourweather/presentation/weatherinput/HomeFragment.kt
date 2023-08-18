package com.umc.yourweather.presentation.weatherinput


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.umc.yourweather.R
import com.umc.yourweather.data.enums.Status
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.HomeResponse
import com.umc.yourweather.data.service.WeatherService
import com.umc.yourweather.databinding.FragmentHomeBinding
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.presentation.share.CarmeraPermissionFragment
import com.umc.yourweather.presentation.share.SharedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response

class HomeFragment : Fragment(), HomeFragmentInteractionListener {
    private lateinit var binding: FragmentHomeBinding
    private val weatherService = RetrofitImpl.authenticatedRetrofit.create(WeatherService::class.java)
    private lateinit var sharedViewModel: SharedViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)


        sharedViewModel.hideViewsEvent.observe(viewLifecycleOwner) {
            hideViews()
        }

        sharedViewModel.showViewsEvent.observe(viewLifecycleOwner) {
            showViews()
        }

        binding.btnHomeShare.setOnClickListener {
            // 카메라권한설정프래그먼트띄우기
            val fragment = CarmeraPermissionFragment()
            parentFragmentManager.commit {
                replace(R.id.fl_home_l1, fragment)
                addToBackStack(null)
            }
        }

        // 광고 뷰 이동버튼 클릭
        binding.btnHomeAdMove.setOnClickListener {
            val url = "https://yourweather.shop:8080/api/v1/ad/get-advertisement"

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        
        binding.btnHomeAdExit.setOnClickListener {
            hideAdViews()
        }

        binding.flHomeAdExit.setOnClickListener {
            hideAdViews()
        }

        binding.btnHomeWeatherinput.setOnClickListener {
            openHomeWeatherInputFragment()
        }
        fetchHomeDataAndHandleResponse()
    } 
    

    // 광고 뷰 숨기기
    private fun hideAdViews() {
        binding.tvHomeAd.visibility = View.GONE
        binding.btnHomeAdMove.visibility = View.GONE
        binding.flHomeAdExit.visibility = View.GONE
        binding.btnHomeAdExit.visibility = View.GONE
    }

    private fun openHomeWeatherInputFragment() {
        val fragment = HomeWeatherInputFragment()
        fragment.setListener(this)
        parentFragmentManager.commit {
            replace(R.id.fl_home_l1, fragment)
            addToBackStack(null)
        }
    }

    override fun goToNewHome() {
        fetchHomeDataAndHandleResponse()
        parentFragmentManager.popBackStackImmediate()
        showHomeToast()
    }

    private fun fetchHomeDataAndHandleResponse() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val call: Call<BaseResponse<HomeResponse>> = weatherService.getHomeData()
                val response: Response<BaseResponse<HomeResponse>> = call.execute()

                if (response.isSuccessful && response.body() != null) {
                    val baseResponse = response.body()!!
                    val homeResponse = baseResponse.result
                    if (homeResponse != null) {
                        withContext(Dispatchers.Main) {
                            handleHomeResponse(homeResponse)
                            updateUIWithHomeResponse(homeResponse)
                        }
                    } else {
                        Log.e("HomeFragment", "홈 데이터가 null입니다.")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("HomeFragment", "홈 데이터 가져오기 실패: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("HomeFragment", "네트워크 오류: ${e.message}")
            }
        }
    }

    private fun handleHomeResponse(homeResponse: HomeResponse) {
        binding.tvHomeUsername.text = homeResponse.nickname
        updateUIWithHomeResponse(homeResponse)
        Log.d("HomeFragment", "홈 닉네임 변경 성공: $homeResponse")
    }

    private fun updateUIWithHomeResponse(homeResponse: HomeResponse) {
        binding.apply {
            val weatherText = when (homeResponse.status) {
                Status.SUNNY -> "맑음"
                Status.CLOUDY -> "구름 약간"
                Status.RAINY -> "비"
                Status.LIGHTNING -> "번개"
                else -> "알 수 없음"
            }

            tvHomeWeather.text = weatherText
            Log.d("HomeFragment", "홈 날씨 설명 변경 성공: $homeResponse")
            tvHomeTemp.text = "${homeResponse.temperature}"
            Log.d("HomeFragment", "홈 온도 변경 성공: $homeResponse")
            updateMotionWeather(homeResponse.status)
            Log.d("HomeFragment", "홈 모션 변경 성공: $homeResponse")
            updateBackgroundImage(homeResponse.imageName)
            Log.d("HomeFragment", "홈 배경 변경 성공: $homeResponse")
            showHomeToast()
            Log.d("HomeFragment", "홈 토스트 출력 성공: $homeResponse")
        }
    }
    
    // 백그라운드 이미지변경
    private fun updateBackgroundImage(imageName: String) {
        val backgroundImageResource = when (imageName) {
            "bg_home1_sunny.jpg" -> R.drawable.bg_home1_sunny
            "bg_home1_cloudy.jpg" -> R.drawable.bg_home1_cloudy
            "bg_home1_rainy.jpg" -> R.drawable.bg_home1_rainy
            "bg_home1_lightning.jpg" -> R.drawable.bg_home1_lightning
            "bg_home2_sunnycloudy.jpg" -> R.drawable.bg_home2_sunnycloudy
            "bg_home2_sunnyrainy.jpg" -> R.drawable.bg_home2_sunnyrainy
            "bg_home2_sunnylightning.jpg" -> R.drawable.bg_home2_sunnylightning
            "bg_home2_rainycloudy.jpg" -> R.drawable.bg_home2_rainycloudy
            "bg_home2_rainylightning.jpg" -> R.drawable.bg_home2_rainylightning
            "bg_home2_lightningcloudy.jpg" -> R.drawable.bg_home2_lightningcloudy
            "bg_home3_sunnycloudyrainy.jpg" -> R.drawable.bg_home3_sunnycloudyrainy
            "bg_home3_sunnycloudylightning.jpg" -> R.drawable.bg_home3_sunnycloudylightning
            "bg_home3_sunnyrainylightning.jpg" -> R.drawable.bg_home3_sunnyrainylightning
            "bg_home3_cloudyrainylightning.jpg" -> R.drawable.bg_home3_cloudyrainylightning
            else -> R.drawable.bg_home4_max
        }
        binding.bgHomeWeather.setImageResource(backgroundImageResource)
    }
    
    // 모션영상 변경
    private fun updateMotionWeather(status: Status) {
        val motionResource = when (status) {
            Status.SUNNY -> R.raw.motion_home_sun
            Status.CLOUDY -> R.raw.motion_home_cloud
            Status.RAINY -> R.raw.motion_home_rain
            Status.LIGHTNING -> R.raw.motion_home_thunder
        }

        Glide.with(requireContext())
            .load(motionResource)
            .into(binding.motionHomeWeather)
    }
    private fun showHomeToast() {
        val customToastView = LayoutInflater.from(requireContext()).inflate(R.layout.toast_home, null)

        val homeToast = Toast(requireContext())
        homeToast.view = customToastView

        homeToast.duration = Toast.LENGTH_SHORT

        homeToast.setGravity(
            android.view.Gravity.BOTTOM or android.view.Gravity.CENTER,
            0,
            resources.getDimensionPixelSize(R.dimen.home_toast_margin_bottom),
        )
    }

    // 캡쳐하기 전 뷰 숨기기
    fun hideViews() {
        binding.tvHomeAd.visibility = View.GONE
        binding.btnHomeAdMove.visibility = View.GONE
        binding.flHomeAdExit.visibility = View.GONE
        binding.btnHomeAdExit.visibility = View.GONE
        binding.btnHomeShare.visibility = View.GONE
        binding.btnHomeWeatherinput.visibility = View.GONE
        binding.bnvMain.visibility = View.GONE
    }
    // 캡쳐 후 뷰 다시 보이기
    fun showViews() {
        binding.tvHomeAd.visibility = View.VISIBLE
        binding.btnHomeAdMove.visibility = View.VISIBLE
        binding.flHomeAdExit.visibility = View.VISIBLE
        binding.btnHomeAdExit.visibility = View.VISIBLE
        binding.btnHomeShare.visibility = View.VISIBLE
        binding.btnHomeWeatherinput.visibility = View.VISIBLE
        binding.bnvMain.visibility = View.VISIBLE
    }


}
