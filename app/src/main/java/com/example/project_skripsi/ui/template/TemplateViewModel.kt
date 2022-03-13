package com.example.project_skripsi.ui.template

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TemplateViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is template Fragment"
    }
    val text: LiveData<String> = _text
}