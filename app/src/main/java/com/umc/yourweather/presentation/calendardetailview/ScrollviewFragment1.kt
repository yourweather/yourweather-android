package com.umc.yourweather.presentation.calendardetailview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.umc.yourweather.R
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.MemoDailyResponse
import com.umc.yourweather.data.service.MemoService
import com.umc.yourweather.databinding.FragmentScrollview1Binding
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.presentation.adapter.CalendarDetailviewDiaryAdapter
import retrofit2.Call
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ScrollviewFragment1.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScrollviewFragment1 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var binding: FragmentScrollview1Binding
    private lateinit var adapter: CalendarDetailviewDiaryAdapter
    private lateinit var rootView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_scrollview1, container, false)
        binding = FragmentScrollview1Binding.bind(rootView)

        val selectedDate = arguments?.getString("selectedDate")
        binding.tvScrollviewFragment11.text = selectedDate

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val weatherId = requireActivity().intent.getIntExtra("weatherId", -1)
        ScrollviewFragment1Api(weatherId) // -1은 기본값, 원하는 값으로 설정해주세요

        // weatherId를 활용하여 API 요청 보내기
        if (weatherId != -1) {
            ScrollviewFragment1Api(weatherId)
        }
        // 그 외의 UI 초기화나 설정 등을 진행할 수 있습니다.
    }
    private fun ScrollviewFragment1Api(weatherId: Int) {
        val memoService = RetrofitImpl.authenticatedRetrofit.create(MemoService::class.java)
        val call = memoService.memoReturn(weatherId = weatherId)

        call.enqueue(object : retrofit2.Callback<BaseResponse<MemoDailyResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<MemoDailyResponse>>,
                response: Response<BaseResponse<MemoDailyResponse>>,
            ) {
//                if (response.isSuccessful) {
//                    val outerResponse = response.body()?.result
//                    val memoDailyResponse = outerResponse?.result
//
//                    if (memoDailyResponse != null) {
//                        val memoContentList = memoDailyResponse.memoContentList
//
//                        // 여기에서 사용자 정보 활용하여 작업 수행
//                        if (memoContentList.isNotEmpty()) {
//                            val firstMemoContent = memoContentList[0] // Use the first memo content item
//                            val memoId = firstMemoContent.memoId
//                            val creationDatetime = firstMemoContent.dateTime
//                            val content = firstMemoContent.status
//                            // 여기에서 memoItem의 필드 값을 활용하여 작업 수행
//                            viewAdapter = CalendarDetailviewDiaryAdapter(memoContentList)
//
//                            // Initialize the RecyclerView and its components
//                            recyclerView = rootView.findViewById<RecyclerView>(R.id.rv_scrollview_fragment1_1)
//                            viewManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)
//
//
//                            // Configure the RecyclerView
//                            recyclerView.apply {
//                                // Improve performance if the layout size of the RecyclerView doesn't change
//                                setHasFixedSize(true)
//                                // Use a linear layout manager
//                                layoutManager = viewManager
//                                // Specify an adapter
//                                adapter = viewAdapter
//                            }
//
//                            val dateString = firstMemoContent.dateTime
//                            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
//                            val date1 = dateFormat.parse(dateString.toString())
//                            val date2 = dateFormat.parse(creationDatetime)
//                            val calendar = Calendar.getInstance()
//                            calendar.time = date1
//
//
//                            val month = calendar.get(Calendar.MONTH) + 1 // 월은 0부터 시작하므로 +1을 해줌
//                            val day = calendar.get(Calendar.DAY_OF_MONTH)
//
//                            binding.tvScrollviewFragment11.text=("${month}월 ${day}일의 일기" )
//
//                            // Apply the desired time format
//                            val outputFormat = SimpleDateFormat("a h시", Locale.US)
//                            val formattedTime = outputFormat.format(date2)
//
//                            binding.tvScrollviewFragmentTime.text=formattedTime
//
//                        }
//
//                        else {
//                            // 서버 응답은 성공했지만 데이터가 없는 경우 처리
//                            Log.e("API Response", "No memo data for the requested date")
//                        }
//                    } else {
//                        // 서버 응답이 실패한 경우 처리
//                        Log.e("API Response", "Failed to retrieve memo data")
//                    }
//                }
            }
            override fun onFailure(
                call: Call<BaseResponse<MemoDailyResponse>>,
                t: Throwable,
            ) {
                // 네트워크 요청 실패 처리
                Log.e("API Failure", "Error: ${t.message}", t)
                // 사용자에게 오류 메시지 표시
                val errorMessage = "네트워크 요청이 실패했습니다."
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ScrollviewFragment1.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ScrollviewFragment1().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
