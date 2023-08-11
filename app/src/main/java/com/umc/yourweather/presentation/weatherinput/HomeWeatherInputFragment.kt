package com.umc.yourweather.presentation.weatherinput

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.umc.yourweather.R
import com.umc.yourweather.data.remote.request.MemoRequest
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.MemoResponse
import com.umc.yourweather.data.service.MemoService
import com.umc.yourweather.databinding.FragmentHomeWeatherInputBinding
import com.umc.yourweather.di.RetrofitImpl
import retrofit2.Call
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeWeatherInputFragment : Fragment() {

    private lateinit var binding: FragmentHomeWeatherInputBinding
    private var listener: HomeFragmentInteractionListener? = null
    private var isButtonClicked = false
    private var isSeekBarAdjusted = false

    fun setListener(listener: HomeFragmentInteractionListener) {
        this.listener = listener
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeWeatherInputBinding.inflate(inflater, container, false)
        setupUI()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupUI() {
        val buttonAnimation: Animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.btn_weather_scale)

        binding.btnHomeSun.setOnClickListener {
            clearAnimations()
            it.startAnimation(buttonAnimation)
            isButtonClicked = true
            updateSaveButtonState()
            // 향후 클릭 시 추가할 동작 설정
        }

        binding.btnHomeCloud.setOnClickListener {
            clearAnimations()
            it.startAnimation(buttonAnimation)
            isButtonClicked = true
            updateSaveButtonState()
        }

        binding.btnHomeThunder.setOnClickListener {
            clearAnimations()
            it.startAnimation(buttonAnimation)
            isButtonClicked = true
            updateSaveButtonState()
        }

        binding.btnHomeRain.setOnClickListener {
            clearAnimations()
            it.startAnimation(buttonAnimation)
            isButtonClicked = true
            updateSaveButtonState()
        }

        // exit 버튼 직접 클릭한 경우
        binding.btnHomeWeatherExit.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        // exit 버튼이 담긴 영역 클릭한 경우
        binding.flWeatherInputExit.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // save 버튼 직접 클릭한 경우
        binding.btnHomeWeatherSave.setOnClickListener {
            // 버튼이 활성화된 경우에만 클릭 리스너 동작
            if (isButtonClicked && isSeekBarAdjusted) {
                updateUserInputsAndCallApi()
            }
        }

        // save 버튼이 담긴 영역 클릭한 경우
        binding.flWeatherInputSave.setOnClickListener {
            // 버튼이 활성화된 경우에만 클릭 리스너 동작
            if (isButtonClicked && isSeekBarAdjusted) {
                updateUserInputsAndCallApi()
            }
        }

        binding.seekbarHomeTemp.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                isSeekBarAdjusted = fromUser
                updateSaveButtonState()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // 필요한 경우 구현
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // 필요한 경우 구현
            }
        })
    }


    private fun clearAnimations() {
        binding.btnHomeSun.clearAnimation()
        binding.btnHomeCloud.clearAnimation()
        binding.btnHomeThunder.clearAnimation()
        binding.btnHomeRain.clearAnimation()
    }

    private fun updateSaveButtonState() {
        val context = requireContext()
        val isActive = isButtonClicked && isSeekBarAdjusted

        binding.btnHomeWeatherSave.isEnabled = isActive
        if (isActive) {
            binding.btnHomeWeatherSave.setImageResource(R.drawable.ic_home_save)
        } else {
            binding.btnHomeWeatherSave.setImageResource(R.drawable.ic_home_unsave)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateUserInputsAndCallApi() {
        val status = when {
            binding.btnHomeSun.isSelected -> MemoRequest.Status.SUNNY
            binding.btnHomeCloud.isSelected -> MemoRequest.Status.CLOUDY
            binding.btnHomeRain.isSelected -> MemoRequest.Status.RAINY
            binding.btnHomeThunder.isSelected -> MemoRequest.Status.LIGHTNING
            else -> MemoRequest.Status.SUNNY // 기본값 설정
        }

        val content = binding.etHomeMemo.text.toString()

        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)

        val temperature = binding.seekbarHomeTemp.progress // SeekBar 등에서 입력 받음

        val memoRequest = MemoRequest(
            status = status,
            content = content,
            localDateTime = formattedDateTime,
            temperature = temperature
        )

        callMemoWriteApi(memoRequest)
    }



    private fun callMemoWriteApi(request: MemoRequest) {
        val memoService = RetrofitImpl.authenticatedRetrofit.create(MemoService::class.java)

        val call = memoService.memoWrite(request)

        call.enqueue(object : retrofit2.Callback<BaseResponse<MemoResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<MemoResponse>>,
                response: retrofit2.Response<BaseResponse<MemoResponse>>
            ) {
                if (response.isSuccessful) {
                    // API 호출 성공 시 처리
                    val memoResponse = response.body() // MemoResponse 데이터 추출
                    Log.d("weatherInput", "memo 저장 성공")
                    goToNewHome() // 새로운 홈 화면으로 이동
                } else {
                    // API 호출 실패 시 처리
                    val errorBody = response.errorBody() // 에러 응답 데이터 추출
                    Log.e("weatherInput", "memo 저장 실패: ${errorBody?.string()}")
                }
            }

            override fun onFailure(call: Call<BaseResponse<MemoResponse>>, t: Throwable) {
                // 네트워크 오류 처리
                t.printStackTrace()
                Log.e("weatherInput", "memo 저장 실패: ${t.message}")
            }
        })
    }

    private fun goToNewHome() {
        val homeFragment = HomeFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fl_initial_l1, homeFragment)
            .addToBackStack(null)
            .commit()
    }
}