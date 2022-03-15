package com.example.project_skripsi.module.student.subject_detail

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StSubjectViewModel : ViewModel() {

    companion object {
        const val tabCount = 4
        val tabHeader = arrayOf("Absen", "Materi", "Ujian", "Tugas")
    }

    private val _attendanceList = MutableLiveData<List<String>>()
    val attendanceList : LiveData<List<String>> = _attendanceList

    private val _resourceList = MutableLiveData<List<String>>()
    val resourceList : LiveData<List<String>> = _resourceList

    private val _examList = MutableLiveData<List<String>>()
    val examList : LiveData<List<String>> = _examList

    private val _assignmentList = MutableLiveData<List<String>>()
    val assignmentList : LiveData<List<String>> = _assignmentList

    init {
        Handler(Looper.getMainLooper()).postDelayed({
            _attendanceList.value = listOf("hadir", "hadir", "absen")
            _resourceList.value = listOf("materi susah", "materi penting", "materi kelulusan")
            _examList.value = listOf("tebak-tebak", "mikir", "berantem")
            _assignmentList.value = listOf("cari nyamuk", "cari kecoa", "cari air")
        }, 1000)
    }

    fun getSubjectData(subjectId : String) {
        Log.d("12345", "call $subjectId")
    }

}