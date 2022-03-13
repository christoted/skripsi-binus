package com.example.project_skripsi.ui.student.subject_detail.attendance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StSubjectAttendanceViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is attendance Fragment"
    }
    val text: LiveData<String> = _text
}