package com.example.project_skripsi.module.student

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.utils.generic.HandledEvent

class StMainViewModel : ViewModel() {

    private val _isImageCaptured = MutableLiveData<HandledEvent<Boolean>>()
    val isImageCaptured : LiveData<HandledEvent<Boolean>> = _isImageCaptured

    fun captureImage(){
        _isImageCaptured.postValue(HandledEvent(true))
    }

}