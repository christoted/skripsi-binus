package com.example.project_skripsi.module.student.task.form

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StTaskFormViewModel : ViewModel() {

    private val _formQuestion = MutableLiveData<List<Int>>()
    val formQuestion : LiveData<List<Int>> = _formQuestion

    init {
        Handler(Looper.getMainLooper()).postDelayed({
            _formQuestion.value = listOf(1,0,1,1,1,1,0,0)
        }, 2000)
    }

}