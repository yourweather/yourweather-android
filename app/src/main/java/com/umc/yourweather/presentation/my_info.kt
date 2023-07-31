package com.umc.yourweather.presentation

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivityMyInfoBinding
import com.umc.yourweather.databinding.DialogLogoutBinding

class my_info : AppCompatActivity() {
    lateinit var binding: ActivityMyInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        binding.logout.setOnClickListener {
            val dialogBinding = DialogLogoutBinding.inflate(layoutInflater)
            val dialog = Dialog(this, R.style.CustomDialogTheme)
            dialog.setContentView(dialogBinding.root)

            dialogBinding.cancel.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        binding.withdraw.setOnClickListener {
            val i = Intent(this, WithdrawPage::class.java)
            startActivity(i)
        }

        binding.ivBack.setOnClickListener {
            finish()
        }
    }
}
