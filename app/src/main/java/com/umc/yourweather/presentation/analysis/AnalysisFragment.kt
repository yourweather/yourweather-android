package com.umc.yourweather.presentation.analysis

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.yourweather.R
import com.umc.yourweather.R.drawable
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.MissedInputResponse
import com.umc.yourweather.data.service.ReportService
import com.umc.yourweather.databinding.FragmentAnalysisBinding
import com.umc.yourweather.di.RetrofitImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("DEPRECATION")
class AnalysisFragment : Fragment() {
    private var _binding: FragmentAnalysisBinding? = null
    private val binding get() = _binding!!

    private var isMonthlySelected = true
    private var isWeeklySelected = false

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentAnalysisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val normalBackground: Drawable = resources.getDrawable(drawable.btn_brown_rec)
        val pressedBackground: Drawable = resources.getDrawable(drawable.btn_transp_rec)

        CoroutineScope(Dispatchers.Main).launch {
            val result = unWrittenApi()
            if (result) { // 결과확인용

                Log.d("코루틴 반환값 확인", "$result")
                Log.d("코루틴 시작 메인함수 ", "결과옴")
                Log.d("코루틴 시작 메인함수", "프래그먼트 시작")
                viewMonthly()
            } else {
                Log.d("코루틴 반환값 확인", "$result")
                Log.d("코루틴 시작 메인함수 ", "결과옴")
                Log.d("코루틴 시작 메인함수", "프래그먼트")
                viewMonthly()
            }
        }
        binding.btnAnalysisMonthly.setOnClickListener {
            if (isMonthlySelected) {
                return@setOnClickListener // Already selected, do nothing
            }
            isMonthlySelected = true
            isWeeklySelected = false

            viewMonthly()

            if (binding.btnAnalysisMonthly.background == normalBackground) {
                binding.btnAnalysisMonthly.background = pressedBackground
                binding.btnAnalysisMonthly.setTextColor(resources.getColor(android.R.color.black))
                binding.btnAnalysisWeekly.background = normalBackground
                binding.btnAnalysisWeekly.setTextColor(resources.getColor(android.R.color.white))
            } else {
                binding.btnAnalysisMonthly.background = normalBackground
                binding.btnAnalysisWeekly.background = pressedBackground
                binding.btnAnalysisMonthly.setTextColor(resources.getColor(android.R.color.white))
                binding.btnAnalysisWeekly.setTextColor(resources.getColor(android.R.color.black))
            }
        }

        binding.btnAnalysisWeekly.setOnClickListener {
            if (isWeeklySelected) {
                return@setOnClickListener // Already selected, do nothing
            }

            isWeeklySelected = true
            isMonthlySelected = false
            viewWeekly()

            if (binding.btnAnalysisWeekly.background === normalBackground) {
                binding.btnAnalysisWeekly.background = pressedBackground
                binding.btnAnalysisWeekly.setTextColor(resources.getColor(android.R.color.black))
                binding.btnAnalysisMonthly.background = normalBackground
                binding.btnAnalysisMonthly.setTextColor(resources.getColor(android.R.color.white))
            } else {
                binding.btnAnalysisWeekly.background = normalBackground
                binding.btnAnalysisWeekly.setTextColor(resources.getColor(android.R.color.white))
                binding.btnAnalysisMonthly.background = pressedBackground
                binding.btnAnalysisMonthly.setTextColor(resources.getColor(android.R.color.black))
            }
        }

        /**
         * binding.btnBell.setOnClickListener {
         val mFragment = AllWrittenFragment()
         requireActivity().supportFragmentManager.beginTransaction()
         .replace(R.id.fl_content, mFragment)
         .addToBackStack(null)
         .commit()
         }
         * */
    }

    private fun viewMonthly() {
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.f_barStatic, BarStaticsMonthlyFragment())
        transaction.replace(R.id.f_iconStatic, IconStaticsMonthlyFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun viewWeekly() {
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.f_barStatic, BarStaticsWeeklyFragment())
        transaction.replace(R.id.f_iconStatic, IconStaticsWeeklyFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    // 미입력 내역 호출 API
    private suspend fun unWrittenApi(): Boolean {
        val service = RetrofitImpl.authenticatedRetrofit.create(ReportService::class.java)
        val call = service
        return suspendCancellableCoroutine { continuation ->
            val call = service.noInput()
            call.enqueue(object : Callback<BaseResponse<MissedInputResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<MissedInputResponse>>,
                    response: Response<BaseResponse<MissedInputResponse>>,
                ) {
                    if (response.isSuccessful) {
                        val baseResponse = response.body() // BaseResponse 객체 가져오기
                        Log.d("baseResponse", "baseResponse: $baseResponse")

                        if (baseResponse != null) {
                            if (baseResponse.success) {
                                val missedInputResponse = baseResponse.result
                                if (missedInputResponse != null) {
                                    val localDates = missedInputResponse.localDates
                                    Log.d("미입력 내역 Success", "localDates: $localDates")
                                    Log.d("코루틴 시작 요청함수 ", "요청시작")
                                    Log.d("코루틴 시작 요청함수", "결과옴")

                                    if (localDates.isEmpty()) {
                                        binding.btnBell.setOnClickListener {
//                                            val mFragment = AllWrittenFragment()
//                                            requireActivity().supportFragmentManager.beginTransaction()
//                                                .replace(R.id.fl_content, mFragment)
//                                                .addToBackStack(null)
//                                                .commit()
                                            val intent = Intent(requireContext(), AllWrittenActivity::class.java)
                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                            startActivity(intent)
                                        }
                                    } else {
                                        binding.imgBellEvent.visibility = View.VISIBLE
                                        binding.btnBell.setOnClickListener {
                                            val intent = Intent(requireContext(), UnwrittenDetailListActivity::class.java)
                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                            startActivity(intent)
                                        }
                                    }
                                    return continuation.resume(true, null)
                                } else {
                                    Log.e("Error (null)", "Response body 비었음")
                                }
                            } else {
                                Log.e("API Error", "API 호출 성공, 하지만 success 값이 false입니다.")
                            }
                        } else {
                            Log.e("Error (null)", "Response body 비었음")
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("API Error", "Response Code: ${response.code()}, Error Body: $errorBody")
                    }
                    continuation.resume(false, null)
                }

                override fun onFailure(call: Call<BaseResponse<MissedInputResponse>>, t: Throwable) {
                    continuation.resume(false, null)
                    Log.e("API Failure", "Error: ${t.message}", t)
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
