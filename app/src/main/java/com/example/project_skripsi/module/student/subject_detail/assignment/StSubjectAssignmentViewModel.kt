package com.example.project_skripsi.module.student.subject_detail.assignment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StSubjectAssignmentViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is assignment Fragment"
    }
    val text: LiveData<String> = _text
}