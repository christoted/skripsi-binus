package com.example.project_skripsi.ui.student.main.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StCalendarViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is calendar Fragment"
    }
    val text: LiveData<String> = _text

}