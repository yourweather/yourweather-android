package com.umc.yourweather.presentation.mypage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.yourweather.databinding.FragmentAlertdialogMypageLogoutBinding
import com.umc.yourweather.di.UserSharedPreferences
import com.umc.yourweather.di.TokenSharedPreferences
import com.umc.yourweather.presentation.sign.SignInActivity

class MyPageLogoutFragment : Fragment() {
    private var _binding: FragmentAlertdialogMypageLogoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentAlertdialogMypageLogoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logoutBtn.setOnClickListener {
            // 기기에 저장된 비밀번호와 이메일 정보 삭제
            UserSharedPreferences.clearUser(requireContext())
            val tokenPrefs = TokenSharedPreferences(requireContext())
            tokenPrefs.clearTokens()

            // 로그인 창으로 이동
            val intent = Intent(requireContext(), SignInActivity::class.java)
            startActivity(intent)
            requireActivity().finish() // requireActivity()로 액티비티에 접근
        }

        binding.logoutBtnCancel.setOnClickListener {
            requireActivity().finish()
        }
    }
}
