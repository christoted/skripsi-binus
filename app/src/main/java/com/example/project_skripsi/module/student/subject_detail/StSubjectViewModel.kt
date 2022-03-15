package com.example.project_skripsi.module.student.subject_detail

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel

class StSubjectViewModel : ViewModel() {

    companion object {
        const val tabCount = 4
        val tabHeader = arrayOf("Absen", "Materi", "Ujian", "Tugas")
    }

    fun getSubjectData(subjectId : String) {
        Log.d("12345", "call $subjectId")
    }

}