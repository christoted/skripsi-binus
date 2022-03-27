package com.example.project_skripsi.module.student.main.studyclass

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.min

class StClassViewModel : ViewModel() {

    private val _subjectList = MutableLiveData<List<String>>()
    val subjectList: LiveData<List<String>> = _subjectList

    init {
        Handler(Looper.getMainLooper()).postDelayed({
            _subjectList.value = listOf(
                "Matematika",
                "Fisika",
                "Kimia",
                "Biologi",
                "B.Indonesia",
                "B.Inggris",
                "B.Mand",
                "Sejarah",
                "Ekonomi",
                "PKn",
                "Sosiologi",
                "Geografi",
                "Kesenian",
                "Bisnis",
                "IT",
                "Kuliner",
                "SI",
                "Mobile",
                "Game",
                "Cyber",
                "AI",
                "Cloud",
                "Finance",
                "Dokter",
                "Pengacara"
            )
        }, 1000)
    }

    fun refreshStudyClass(){

    }


    fun getSubjects(page: Int): List<String> {
        val startIdx = page * 8
        val endIdx = min(startIdx + 8, subjectList.value?.size?:0)
        return subjectList.value?.subList(startIdx, endIdx)?: listOf()
    }

    fun getSubjectPageCount() : Int{
        val subjects = subjectList.value?.size ?: 0
        return (subjects + 8 - 1) / 8
    }

}