package com.umc.yourweather.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.yourweather.R
import com.umc.yourweather.databinding.FragmentHomeWeatherInputBinding

class HomeWeatherInputFragment : Fragment() {
    private lateinit var binding: FragmentHomeWeatherInputBinding
    private var listener: HomeFragmentInteractionListener? = null

    fun setListener(listener: HomeFragmentInteractionListener) {
        this.listener = listener
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeWeatherInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnHomeWeatherExit.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnHomeWeatherSave.setOnClickListener {
            val homeFragment = HomeFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fl_initial_l1, homeFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}