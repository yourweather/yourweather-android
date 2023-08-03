
package com.umc.yourweather.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.umc.yourweather.R
import com.umc.yourweather.databinding.FragmentInitialNoWeatherBinding

class InitialNoWeatherFragment : Fragment() {
    private lateinit var binding: FragmentInitialNoWeatherBinding

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

        showInitialToast()

        binding.btnInitialWeather.setOnClickListener {
            val newFragment = HomeWeatherInputFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fl_initial_l1, newFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
    private fun showInitialToast() {
        val toastView = layoutInflater.inflate(R.layout.toast_initial, binding.root, false)
        val toast = Toast.makeText(context, "", Toast.LENGTH_LONG)
        toast.view = toastView
        toast.show()
    }
}
