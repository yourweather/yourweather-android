package com.umc.yourweather.presentation.analysis

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.yourweather.R
import com.umc.yourweather.data.entity.ItemUnwritten
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.MissedInputResponse
import com.umc.yourweather.data.service.ReportService
import com.umc.yourweather.databinding.FragmentUnwrittenDetailListBinding
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.presentation.adapter.UnwrittenRVAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class UnwrittenDetailListFragment : Fragment() {
    private var _binding: FragmentUnwrittenDetailListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentUnwrittenDetailListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뒤로가기
        binding.rlBtn1.setOnClickListener {
            navigateToAnalysisFragment()
        }

        // 뒤로가기 버튼을 누를 때 AnalysisFragment로 이동하도록 설정
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToAnalysisFragment()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        unWrittenApi()
    }

    private fun getDayOfWeek(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day) // Note: month is 0-indexed
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        val daysOfWeek = arrayOf("일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일")
        return daysOfWeek[dayOfWeek - 1]
    }

    // 문자열 포맷 함수
    private fun getFormattedDate(month: Int, day: Int): String {
        return "${month}월 ${day}일"
    }

    private fun fetchDataFromAPI(localDates: List<String>): List<ItemUnwritten> {
        val dataList = mutableListOf<ItemUnwritten>()

        for (date in localDates) {
            val splitDate = date.split("-")
            if (splitDate.size == 3) {
                val year = splitDate[0].toInt()
                val month = splitDate[1].toInt()
                val day = splitDate[2].toInt()

                val dayOfWeek = getDayOfWeek(year, month, day)
                val formattedDate = getFormattedDate(month, day)
                val formattedItem = "$formattedDate $dayOfWeek"

                dataList.add(ItemUnwritten(formattedItem))
            }
        }

        return dataList
    }

    private fun navigateToAnalysisFragment() {
        val analysisFragment = AnalysisFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fl_content, analysisFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun unWrittenApi() {
        val service = RetrofitImpl.authenticatedRetrofit.create(ReportService::class.java)
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

                                val dataList = fetchDataFromAPI(localDates)

                                binding.recyclerViewUnwrittenDetail.layoutManager = LinearLayoutManager(requireContext())
                                val adapter = UnwrittenRVAdapter(dataList, localDates, requireContext())
                                binding.recyclerViewUnwrittenDetail.adapter = adapter
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
            }

            override fun onFailure(call: Call<BaseResponse<MissedInputResponse>>, t: Throwable) {
                Log.e("API Failure", "Error: ${t.message}", t)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
