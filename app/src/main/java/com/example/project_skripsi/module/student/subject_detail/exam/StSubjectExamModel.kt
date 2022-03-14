package com.example.project_skripsi.module.student.subject_detail.exam

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StSubjectExamModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is exam Fragment"
    }
    val text: LiveData<String> = _text
}