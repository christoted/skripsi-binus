package com.example.project_skripsi.ui.student.subject_detail.resource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StSubjectResourceViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is res Fragment"
    }
    val text: LiveData<String> = _text
}