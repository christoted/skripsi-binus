package com.example.project_skripsi.module.student.task.assignment

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_skripsi.module.student.task.exam.StTaskExamViewModel

class StTaskAssignmentViewModel : ViewModel() {

    companion object {
        const val ASSIGNMENT_ONGOING = 0
        const val ASSIGNMENT_PAST = 1
        const val tabCount = 2
        val tabHeader = arrayOf("Berlangsung", "Selesai")
    }

    private val _list = MutableLiveData<List<String>>()
    val list : LiveData<List<String>> = _list


    init {
        Handler(Looper.getMainLooper()).postDelayed({
            _list.value = listOf("XII-IPA-3", "XII-IPA-5")
        }, 1000)
    }

    fun getAssignmentList(position: Int) : List<String> =
        when (position) {
            StTaskExamViewModel.EXAM_ONGOING -> list.value ?: emptyList()
            StTaskExamViewModel.EXAM_PAST -> list.value ?: emptyList()
            else -> emptyList()
        }
}