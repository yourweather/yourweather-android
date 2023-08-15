package com.umc.yourweather.presentation.calendardetailview

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.cardview.widget.CardView
import com.umc.yourweather.R
import com.umc.yourweather.data.enums.Status
import com.umc.yourweather.data.remote.request.MemoUpdateRequest
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.MemoUpdateResponse
import com.umc.yourweather.data.service.MemoService
import com.umc.yourweather.databinding.ActivityCalendarDetailviewModify1Binding
import com.umc.yourweather.di.RetrofitImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalendarDetailviewModify1 : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarDetailviewModify1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarDetailviewModify1Binding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    // 메모 삭제 API
    private fun deleteMemo(memoIdToDelete: Int) {
        val memoService = RetrofitImpl.authenticatedRetrofit.create(MemoService::class.java)
        memoService.memoDelete(memoIdToDelete).enqueue(object : Callback<BaseResponse<Unit>> {
            override fun onResponse(
                call: Call<BaseResponse<Unit>>,
                response: Response<BaseResponse<Unit>>,
            ) {
                if (response.isSuccessful) {
                    // Memo deletion was successful
                    Log.d("메모 삭제", "메모 삭제, 삭제 전달 성공 ${response.body()?.result}")

                    val baseResponse = response.body()
                    // Handle your success scenario here
                    Log.d("메모 삭제", "메모 삭제, 삭제 전달 실패 ${response.body()?.result}")
                } else {
                    // Memo deletion failed
                    Log.d("메모 삭제 응답 실패", "메모 삭제, 응답 실패 ${response.body()?.result}")
                }
            }

            override fun onFailure(call: Call<BaseResponse<Unit>>, t: Throwable) {
                // Handle failure (network issue, etc.)
            }
        })
    }

    // 메모 수정 API
    private fun updateMemo(memoId: Int, status: Status, temperature: Int, content: String) {
        val memoService = RetrofitImpl.authenticatedRetrofit.create(MemoService::class.java)

        val updateRequest = MemoUpdateRequest(status, temperature, content)

        memoService.memoUpdate(memoId, updateRequest).enqueue(object :
            Callback<BaseResponse<MemoUpdateResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<MemoUpdateResponse>>,
                response: Response<BaseResponse<MemoUpdateResponse>>,
            ) {
                if (response.isSuccessful) {
                    // Memo update was successful
                    val baseResponse = response.body()
                    Log.d("메모 수정", "메모 수정, 수정 전달 성공 ${response.body()?.result}")
                } else {
                    Log.d("메모 수정", "메모 수정 전달 실패 ${response.body()?.result}")
                    val errorResponse = response.errorBody()?.string()
                    // Handle your error scenario here
                }
            }

            override fun onFailure(call: Call<BaseResponse<MemoUpdateResponse>>, t: Throwable) {
                // Handle failure (network issue, etc.)
            }
        })
    }
}
