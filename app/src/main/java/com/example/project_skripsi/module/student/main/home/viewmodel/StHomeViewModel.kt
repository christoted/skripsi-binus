package com.example.project_skripsi.module.student.main.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// Dummy Class
data class HomeMainSection(val sectionName: String, val sectionItem: List<String>)

class StHomeViewModel : ViewModel() {

    private val _profileName = MutableLiveData<String>()
    val profileName : LiveData<String> = _profileName

    private val _profileClass = MutableLiveData<String>()
    val profileClass : LiveData<String> = _profileClass

    private val _sectionDatas = MutableLiveData<List<HomeMainSection>>()
    val sectionDatas: LiveData<List<HomeMainSection>> = _sectionDatas

//    private val _profilePicture = MutableLiveData<String>()
//    val profilePicture : LiveData<String> = _profilePicture

    init {
        _profileName.value = "Luis Anthonie Alkins (21)"
        _profileClass.value = "XII - IPA - 1"
        initData()
    }

    private fun initData() {
        val listSchedule = arrayListOf<String>()
        listSchedule.add("Bahasa Inggris")
        listSchedule.add("Matematika")

        val listExam = arrayListOf<String>()
        listExam.add("Biology's Exam")
        listExam.add("Math's Exam")

        val listDatas = arrayListOf<HomeMainSection>()
        listDatas.add(HomeMainSection("Class Schedule", sectionItem = listSchedule))
        listDatas.add(HomeMainSection("Exam", sectionItem = listExam))
        _sectionDatas.value = listDatas

    }

}