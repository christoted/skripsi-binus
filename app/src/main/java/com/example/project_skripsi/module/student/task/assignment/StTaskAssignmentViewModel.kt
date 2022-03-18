package com.example.project_skripsi.module.student.task.assignment

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StTaskAssignmentViewModel : ViewModel() {

    private val _list = MutableLiveData<List<String>>()
    val list : LiveData<List<String>> = _list


    init {
        Handler(Looper.getMainLooper()).postDelayed({
            _list.value = listOf("XII-IPA-3", "XII-IPA-5")
        }, 1000)
    }
}