package com.umc.yourweather.presentation.share

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _hideViewsEvent = MutableLiveData<Unit>()
    val hideViewsEvent: LiveData<Unit> = _hideViewsEvent

    private val _showViewsEvent = MutableLiveData<Unit>()
    val showViewsEvent: LiveData<Unit> = _showViewsEvent

    fun hideViews() {
        _hideViewsEvent.value = Unit
    }

    fun showViews() {
        _showViewsEvent.value = Unit
    }
}
