package com.example.project_skripsi.ui.student.main.stclass

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StClassViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is class Fragment"
    }
    val text: LiveData<String> = _text

}