package com.umc.yourweather.presentation.weatherinput

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.umc.yourweather.R
import com.umc.yourweather.data.enums.Status
import com.umc.yourweather.data.remote.request.MemoRequest
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.MemoResponse
import com.umc.yourweather.data.service.MemoService
import com.umc.yourweather.databinding.FragmentHomeWeatherInputBinding
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.di.UserSharedPreferences
import com.umc.yourweather.util.AlertDialogTwoBtn
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
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeWeatherInputBinding.inflate(inflater, container, false)
        setupUI()
        return binding.root
    }
    private fun showMemoCancleDialog() {
        val alertDialog = AlertDialogTwoBtn(requireContext())

        alertDialog.setTitle("감정날씨 입력을 취소하시겠어요?")
        alertDialog.setSubTitle("기록한 내용이 전부 사라집니다.")

        alertDialog.setNegativeButton("아니요") { dialogInterface, _ ->
            dialogInterface.dismiss()

            parentFragmentManager.popBackStackImmediate("HomeWeatherInputFragment", 0)
        }

        alertDialog.setPositiveButton("네") { dialogInterface, _ ->
            dialogInterface.dismiss()

            parentFragmentManager.popBackStack()
        }

        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupUI() {
        val userNickname = UserSharedPreferences.getUserNickname(requireContext())
        binding.tvHomeWeatherUsername2.text = userNickname
        binding.tvHomeWeatherUsername3.text = userNickname

//        binding.btnWeatherinputGuide1.setOnClickListener {
//            val imageView = binding.imgWeatherGuide
//
//            imageView.visibility = View.VISIBLE
//
//            // 이미지를 보여주는 시간
//            val delayDurationMillis = 2000L
//
//            // 일정 시간 후에 이미지뷰를 숨기는 작업
//            imageView.postDelayed({
//                imageView.visibility = View.GONE
//            }, delayDurationMillis)
//        }

        val buttonAnimation: Animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.btn_weather_scale)

        binding.btnHomeSun.setOnClickListener {
            clearAnimations()
            it.startAnimation(buttonAnimation)
            isButtonClicked = true
            updateSaveButtonState()
            updateStatus(Status.SUNNY) // 선택한 버튼에 따라 상태 갱신
        }

        binding.btnHomeCloud.setOnClickListener {
            clearAnimations()
            it.startAnimation(buttonAnimation)
            isButtonClicked = true
            updateSaveButtonState()
            updateStatus(Status.CLOUDY)
        }

        binding.btnHomeThunder.setOnClickListener {
            clearAnimations()
            it.startAnimation(buttonAnimation)
            isButtonClicked = true
            updateSaveButtonState()
            updateStatus(Status.LIGHTNING)
        }

        binding.btnHomeRain.setOnClickListener {
            clearAnimations()
            it.startAnimation(buttonAnimation)
            isButtonClicked = true
            updateSaveButtonState()
            updateStatus(Status.RAINY)
        }

        // exit 버튼 직접 클릭한 경우
        binding.btnHomeWeatherExit.setOnClickListener {
            showMemoCancleDialog()
        }
        // exit 버튼이 담긴 영역 클릭한 경우
        binding.flWeatherInputExit.setOnClickListener {
            showMemoCancleDialog()
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
        val seekbarValueTextView = binding.tvSeekbarValue

        binding.seekbarHomeTemp.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                isSeekBarAdjusted = fromUser
                updateSaveButtonState()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                seekbarValueTextView.visibility = View.INVISIBLE
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val progress = seekBar?.progress ?: 0
                seekbarValueTextView.visibility = View.VISIBLE
                seekbarValueTextView.text = "$progress°"
            }
        })
    }
    private fun updateStatus(status: Status) {
        binding.btnHomeSun.isSelected = status == Status.SUNNY
        binding.btnHomeCloud.isSelected = status == Status.CLOUDY
        binding.btnHomeThunder.isSelected = status == Status.LIGHTNING
        binding.btnHomeRain.isSelected = status == Status.RAINY
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
            binding.btnHomeSun.isSelected -> Status.SUNNY
            binding.btnHomeCloud.isSelected -> Status.CLOUDY
            binding.btnHomeRain.isSelected -> Status.RAINY
            binding.btnHomeThunder.isSelected -> Status.LIGHTNING
            else -> Status.SUNNY // 기본값 설정
        }

        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)

        val temperature = binding.seekbarHomeTemp.progress

        val content = binding.etHomeMemo.text.toString().takeIf { it.isNotBlank() } ?: "" // 메모가 비어있다면 빈 문자열로 처리

        val memoRequest = MemoRequest(
            status = status,
            content = content,
            localDateTime = formattedDateTime,
            temperature = temperature,
        )

        callMemoWriteApi(memoRequest)
    }
    private fun callMemoWriteApi(request: MemoRequest) {
        val memoService = RetrofitImpl.authenticatedRetrofit.create(MemoService::class.java)

        val call = memoService.memoWrite(request)

        call.enqueue(object : retrofit2.Callback<BaseResponse<MemoResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<MemoResponse>>,
                response: retrofit2.Response<BaseResponse<MemoResponse>>,
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
                Log.e("weatherInput", "네트워크오류: ${t.message}")
            }
        })
    }

    private fun goToNewHome() {
        val homeFragment = HomeFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fl_weather_input_l1, homeFragment)
            .addToBackStack(null)
            .commit()

        showHomeToast()
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
        homeToast.show()
    }
}
