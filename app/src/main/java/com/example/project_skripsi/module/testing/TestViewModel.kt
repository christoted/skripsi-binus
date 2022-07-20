package com.example.project_skripsi.module.testing

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TestViewModel : ViewModel() {

    private var vv = 22

    private var _vvLiveData = MutableLiveData<Int>()
    val vvLiveData : LiveData<Int> = _vvLiveData

    init {
        _vvLiveData.postValue(vv)
    }

    fun increase() {
//        vv += 1
        _vvLiveData.postValue(vv)
    }

}