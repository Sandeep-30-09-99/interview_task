package com.example.interviewtask.ui.base

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.interviewtask.util.SingleLiveEvent
import com.example.interviewtask.util.SingleRequestEvent


open class BaseViewModel : ViewModel() {

    val onClick: SingleRequestEvent<View> = SingleRequestEvent<View>()

    override fun onCleared() {
        super.onCleared()
    }

    open fun onClick(view: View) {
        onClick.value = view
    }
}