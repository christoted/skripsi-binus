package com.example.project_skripsi.module.student.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StHomeViewModel : ViewModel() {

    private val _profileName = MutableLiveData<String>()
    val profileName : LiveData<String> = _profileName

    private val _profileClass = MutableLiveData<String>()
    val profileClass : LiveData<String> = _profileClass

//    private val _profilePicture = MutableLiveData<String>()
//    val profilePicture : LiveData<String> = _profilePicture

    init {
        _profileName.value = "Luis Anthonie Alkins (21)"
        _profileClass.value = "XII - IPA - 1"
    }
}