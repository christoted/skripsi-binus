package com.example.project_skripsi.module.teacher.form.assessment

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TcAssessmentTaskFormViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is template Fragment"
    }
    val text: LiveData<String> = _text

    init {
        Handler(Looper.getMainLooper()).postDelayed({
            //Do something after 100ms
        }, 100)
    }

}