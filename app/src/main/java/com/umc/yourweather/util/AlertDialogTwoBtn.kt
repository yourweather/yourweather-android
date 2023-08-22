package com.umc.yourweather.util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.umc.yourweather.R

class AlertDialogTwoBtn(context: Context) : AlertDialog(context) {

    private lateinit var customView: View
    private lateinit var alertText: TextView
    private lateinit var alertSubText: TextView
    private lateinit var btnPositive: Button
    private lateinit var btnNegative: Button

    init {
        customView = LayoutInflater.from(context).inflate(R.layout.alertdialog_twobtn, null)
        alertText = customView.findViewById<TextView>(R.id.tv_alertdailog_twobtn)
        alertSubText = customView.findViewById<TextView>(R.id.tv_alertdailog_sub_title)

        btnNegative = customView.findViewById<Button>(R.id.btn_alertdialog_twobtn_negative)
        btnPositive = customView.findViewById<Button>(R.id.btn_alertdialog_twobtn_positive)

        setView(customView)
    }

    fun setTitle(newTitle: String) {
        if (::customView.isInitialized) {
            alertText.text = newTitle
        }
    }
    fun setSubTitle(newSubTitle: String) {
        if (::customView.isInitialized) {
            alertSubText.text = newSubTitle
        }
    }
    fun setPositiveButton(text: String, listener: DialogInterface.OnClickListener) {
        if (::customView.isInitialized) {
            btnPositive.text = text
            btnPositive.setOnClickListener {
                listener.onClick(this@AlertDialogTwoBtn, DialogInterface.BUTTON_POSITIVE)
            }
        }
    }
    fun setNegativeButton(text: String, listener: DialogInterface.OnClickListener) {
        if (::customView.isInitialized) {
            btnNegative.text = text
            btnNegative.setOnClickListener {
                listener.onClick(this@AlertDialogTwoBtn, DialogInterface.BUTTON_NEGATIVE)
            }
        }
    }

}
